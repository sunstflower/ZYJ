# AGENTS 工作记录

> 本文件是项目的**协作工作日志**。每次工作都要在此追加记录；在业务代码实现过程中**遇到的任何困难都必须记录到本文件**（含问题现象、原因分析、解决方案或当前状态）。

## 使用约定

1. 每次开始工作前，先读本文件了解当前进度与遗留问题。
2. 每次工作结束，在「工作日志」追加一条记录（日期 + 做了什么 + 结果/遗留）。
3. 关键设计决策与权衡记入「设计决策记录」。
4. 遇到困难（卡住、报错、踩坑）**立即**记入「困难与问题记录」，即使尚未解决也要写下现状，便于下次接续。
5. 文档与代码不一致时，以最新结论为准并同步更新对应文档。

---

## 项目快照

- **项目**：健身打卡系统（JSA），分角色（普通用户 / 管理员）演示性大作业。
- **技术栈**：Spring Boot + MyBatis + MySQL；React + Tailwind + react-hot-toast；RESTful API。
- **开发/运行环境**：Mac 开发，Windows 演示，原生运行（JDK + MySQL + 浏览器），不使用 Docker / Electron。
- **设计文档**：见 [`docs/`](./docs/README.md)。

---

## 工作日志

### 2026-06-16 — 接入本机 MySQL8 + 端到端联调通过 ✅

- 本机 MySQL 实测：`mysql` 已切换为 Homebrew **8.0.46**（brew 服务 `mysql@8.0` 已 started），密码 `520213`。`application.yml` 密码已更新。
- 执行 `db/init.sql` 成功：3 表 + 3 用户 + 5 运动项目 + 4 打卡记录（覆盖三状态）。
- `mvn -o spring-boot:run` 启动成功，**0.7s 连上 MySQL 并 Started**。
  - 坑：默认 8080 被 **Docker**（com.docker.backend）占用，启动报 "Port 8080 was already in use"。验证时改用 `--server.port=8081`。演示机若无此冲突仍用 8080；如冲突，改 `application.yml` 的 `server.port` 即可。已记入下方「困难与问题记录」。
- curl 端到端验证全部通过：
  - 登录成功 / 错误密码(1001) / 未带 token(401) ✓
  - 运动项目列表 ✓；提交打卡 ✓；本人记录 ✓
  - 管理员登录 ✓；普通用户访问管理接口(403) ✓；查看全部/待审核 ✓
  - 审核通过 ✓ → DB 中 id=5 状态确为 APPROVED+reviewer_id+review_time ✓
  - 重复审核(1003) ✓；审核不存在记录(1002) ✓
  - AI 建议 Mock 返回 ✓
- 验证后已停止后台进程、清理 app.log。**后端功能确认可用。** 前端尚未实际 `npm install` 跑起来联调（本环境无外网装包），留待用户机器。

### 2026-06-16 — 实现后端业务逻辑 + 编译验证通过

- **Mapper SQL**（替换原 TODO 注释为实际语句）：
  - `UserMapper.xml`：findByUsername / findById。
  - `SportMapper.xml`：findAll / findById。
  - `CheckinRecordMapper.xml`：insert(useGeneratedKeys 回填 id) / findById / findByUser(联表 sport，按状态可选过滤) / findAll(联表 user+sport+审核人) / updateReview。依赖 `map-underscore-to-camel-case` 自动映射，联表列用别名(sport_name/reviewer_nickname)对齐 VO 属性。
- **Service 实现**（替换 UnsupportedOperationException 桩）：
  - `AuthServiceImpl.login`：查用户→明文比对密码→失败抛 LOGIN_FAILED→成功 TokenStore 下发 token + 组装 LoginResponse。
  - `SportServiceImpl.listAll`：findAll → SportVO。
  - `CheckinServiceImpl`：submit(校验项目存在/默认 PENDING/服务端时间/回填 VO，@Transactional)、listMine、listAll、review(校验存在与 PENDING、APPROVE/REJECT→APPROVED/REJECTED、写审核人+时间，@Transactional)。
- **编译验证**：本机无外网且 `3.2.10` 的 jar 从未下载成功（仅有 .pom）；本地缓存中 `3.3.1` 的 jar 完整。故将 Spring Boot `3.2.10→3.3.1`、mybatis-spring-boot-starter `3.0.3→3.0.4`（均 JDK17 基线，跨平台结论不变，见决策 D-9），`mvn -o compile` **编译通过**（37 个类，mapper XML 已入 classpath）。
- **未做**：未实际启动（本环境无 MySQL、无外网）；未跑集成测试。需在装好 JDK17 + MySQL 的机器上 `mvn spring-boot:run` 端到端联调。
- 同步更新：docs/02 技术选型、根 README 的 Spring Boot 版本号 → 3.3.x。

### 2026-06-16 — 搭建前后端工程骨架（按文档）

- **后端**（`backend/`，Maven）：
  - `pom.xml`：Spring Boot 3.2.10 + JDK17 + web/validation + mybatis-spring-boot-starter 3.0.3 + mysql-connector-j。
  - 分层骨架（com.jsa）：`controller`(Auth/Sport/Checkin/Ai)、`service`+`service/impl`、`dao`(Mapper)、`entity`、`dto/request`、`dto/response`。
  - 横切：`common`(Result/ResultCode/BusinessException/AuthSession/TokenStore/CheckinStatus)、`exception/GlobalExceptionHandler`、`config/WebMvcConfig`(CORS+拦截器)、`interceptor/AuthInterceptor`。
  - `resources`：`application.yml`(含 docs/06 的 JDBC 兼容参数)、`mapper/*.xml`(含 resultMap，SQL 以注释 TODO 形式给出)、`db/init.sql`。
  - 业务逻辑（Auth/Sport/Checkin 的 Service 实现 + Mapper SQL）为 **TODO 桩**（抛 UnsupportedOperationException），下一阶段实现。
  - AI：`MockAiAdviceService` 一期已实现（返回模板建议，`@ConditionalOnProperty ai.enabled=false`）。
- **前端**（`frontend/`，Vite）：
  - 配置：`package.json`(React18/router6/axios/react-hot-toast/tailwind3)、`vite.config.js`(/api 代理 8080)、`tailwind/postcss config`、`index.html`。
  - `src`：`main.jsx`(Toaster)、`App.jsx`(路由+守卫)、`api`(http 拦截器/auth/checkin)、`store/auth.js`、`pages`(Login/UserHome/AdminHome)、`components/StatusBadge`。
- **根目录**：`README.md`(启动/打包说明)、`.gitattributes`(换行约定)、`backend/.gitignore`、`frontend/.gitignore`。
- **注意**：原 `README.md`（原始需求原文）被项目 README 覆盖，已将原文存档到 `docs/00-原始需求.md`。
- 结果：骨架完成，结构与文档一致。**尚未实际编译/运行**（本机还未装 JDK17，见待确认项）。

### 2026-06-16 — 记录本机环境 + 跨平台兼容注意事项

- 检测本机（Mac 开发机）实际环境：macOS 15.5 / arm64；`java`=OpenJDK 24，但 Maven 跑在 OpenJDK 23 且 `JAVA_HOME` 未设置（三者不一致）；Maven 3.9.9；Node v25.8.2 / npm 11.11.1；MySQL 9.2.0；Git 2.49.0。
- 新增 `docs/06-本机环境与跨平台兼容.md`：本机环境快照 + Mac→Windows 兼容注意事项。
- 关键结论（影响后续编码，已转为决策 D-7/D-8）：
  - **JDK 统一锁定 17（LTS）**，两端一致；本机需装 JDK 17 并设 `JAVA_HOME`，`pom.xml` 显式声明 `<java.version>17</java.version>`。不用 23/24。
  - **MySQL 统一按 8.0 兼容写法**，`init.sql` 不用 9.x 专有特性；Connector/J 用 8.x；JDBC URL 统一带 `characterEncoding/serverTimezone/allowPublicKeyRetrieval`。
  - arm64→x86_64 对 Java/前端构建产物无影响；约定 `node_modules` 不跨平台拷贝，到 Windows 重新 `npm install`。
- 结果：环境与兼容性记录完成，仍未编写业务代码。

### 2026-06-16 — 文档先行：完成全套设计文档

- 阅读并理解 `README.md` 中的项目要求（健身打卡系统，普通用户/管理员两类角色，打卡+审核流程，预留 AI 建议接口）。
- 在 `docs/` 下完成以下设计文档：
  - `README.md`：文档索引与项目概要、实现状态总览。
  - `01-需求分析.md`：角色、功能性/非功能性需求、PlantUML 用例图、业务规则。
  - `02-架构设计.md`：技术选型、三层架构、前后端目录、统一响应/异常、认证方案、跨平台部署。
  - `03-数据库设计.md`：3 张表（user / sport / checkin_record）、ER 图、外键约束、init.sql。
  - `04-接口文档.md`：全部 RESTful 接口、状态码与错误码约定。
  - `05-AI助手接口设计.md`：AI 健身建议接口预留设计（后期实现）。
- 结果：设计阶段完成，**尚未编写任何业务代码**（按要求文档优先）。
- 下一步建议：搭建后端/前端工程骨架 → 建库执行 init.sql → 实现登录 → 打卡 → 审核 → 前端联调。

---

## 设计决策记录

| 编号 | 决策 | 理由 / 权衡 |
|------|------|-------------|
| D-1 | 不用 Docker / Electron，原生运行 | 标准 B/S Web 应用，演示现场原生运行最简单、最不易出错；Electron 与本架构不匹配。 |
| D-2 | 3 张表，运动项目独立成 `sport` 表 | 体现一对多与外键约束（作业要求）；便于前端下拉选择与数据规范。 |
| D-3 | 管理员复用 `user` 表，用 `role` 区分 | 无需单独管理员表，`reviewer_id` 也指向 user。 |
| D-4 | 认证采用演示级 token（内存映射），预留升级 JWT | 实现简单易讲解；单机演示足够；如需"正规"再换 JWT。 |
| D-5 | 演示打包：前端 build 后并入后端 jar 同源托管 | Windows 上只需 `java -jar` 一条命令，规避跨域与多依赖。 |
| D-6 | AI 接口一期用 `MockAiAdviceService` 返回假数据 | 主体功能不依赖外部服务即可端到端演示；二期再接真实大模型。 |
| D-7 | **JDK 统一锁定 17（LTS）**，两端一致，不用本机的 23/24 | Spring Boot 3 基线，最稳、Windows 最易安装；避免本机 java24/maven23/JAVA_HOME 空的不一致。详见 docs/06。 |
| D-8 | **MySQL 按 8.0 兼容写法**（本机虽为 9.2） | 演示机多为 8.0；init.sql 不用 9.x 专有特性；Connector/J 用 8.x。详见 docs/06。 |
| D-9 | Spring Boot 3.3.1 + mybatis-starter 3.0.4（原计划 3.2.10/3.0.3） | 本机离线，3.2.10 的 jar 未缓存而 3.3.1 完整；同为 JDK17 基线，不影响架构与跨平台结论。 |

---

## 待确认事项（实现前需明确）

- [x] ~~Windows 演示机的 JDK 版本~~ → 已定：两端统一 JDK 17（D-7）。仍需在 Windows 演示机实际安装 JDK 17 并设 `JAVA_HOME`。
- [ ] **本机待办**：安装 JDK 17 并设置 `JAVA_HOME`，使 `java -version` 与 `mvn -version` 都显示 17（当前为 24/23 不一致）。
- [ ] Windows 演示机的 MySQL 实际版本与连接信息（用户名/密码/端口）；按 8.0 兼容验证 `init.sql`。
- [ ] 列表是否需要分页（当前演示数据量小，暂不分页）。
- [ ] 密码是否需要加密存储（演示暂用明文，可升级 BCrypt）。

---

## 困难与问题记录

> 业务代码实现过程中遇到的所有困难记录于此。格式：日期 / 现象 / 原因 / 解决或现状。

### 2026-06-16 / 8080 端口被 Docker 占用
- **现象**：`mvn spring-boot:run` 报 `APPLICATION FAILED TO START ... Port 8080 was already in use`。
- **原因**：本机 Docker（com.docker.backend）正监听 `*:8080`（`lsof -iTCP:8080`）。与后端默认端口冲突，非代码问题。
- **解决**：验证时用 `--server.port=8081` 临时改端口；应用本身正常。演示机若无 Docker 占用可继续用 8080；若占用，改 `application.yml` 的 `server.port`。
