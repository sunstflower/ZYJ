# 健身打卡系统（JSA）

分角色（普通用户 / 管理员）的健身打卡管理系统。课程演示性大作业。

- **后端**：Spring Boot 3.2 + MyBatis + MySQL，RESTful API（JDK 17）
- **前端**：React 18 + Vite + Tailwind CSS + react-hot-toast
- **设计文档**：见 [`docs/`](./docs/README.md)
- **工作日志**：见 [`AGENTS.md`](./AGENTS.md)

## 目录结构

```
JSA/
├── docs/                 # 设计文档（需求/架构/数据库/接口/AI/兼容）
├── backend/              # Spring Boot + MyBatis 后端（Maven）
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/jsa/{controller,service,dao,entity,dto,common,config,interceptor,exception}
│       └── resources/{application.yml, mapper/, db/init.sql}
├── frontend/             # React + Tailwind 前端（Vite）
│   └── src/{api,pages,components,store}
├── AGENTS.md             # 协作工作日志
├── .gitattributes        # 跨平台换行约定
└── README.md
```

> 当前为**工程骨架**：分层、配置、统一响应/异常、路由与接口已就位；核心业务逻辑（登录/打卡/审核的 Service 与 Mapper SQL）为 TODO，待下一阶段实现。AI 建议接口一期已有 Mock 实现。

## 环境要求（两端一致，见 docs/06）

- JDK **17**（不要用 23/24）
- MySQL **8.0+**
- Node.js LTS（仅前端独立开发/构建时需要）

## 本地启动（Mac 开发）

### 1. 数据库

```bash
mysql -u root -p < backend/src/main/resources/db/init.sql
```

### 2. 后端

确认 `backend/src/main/resources/application.yml` 中数据库账号密码，然后：

```bash
cd backend
mvn spring-boot:run
# 启动成功后控制台出现 "Started JsaApplication on port 8080"
```

### 3. 前端

```bash
cd frontend
npm install
npm run dev
# 访问 http://localhost:5173 （/api 已代理到 8080）
```

演示账号：`admin / 123456`（管理员）、`alice / 123456`、`bob / 123456`（普通用户）。

> 注意：核心业务为骨架阶段，登录等接口当前返回「尚未实现」，属预期。

## Windows 演示打包（推荐：前后端合一，见 docs/02 第8/9节）

```bash
# 1. 前端构建静态资源并放入后端 static（同源托管，免跨域、免装 Node）
cd frontend && npm install && npm run build
# 将 frontend/dist/* 拷贝到 backend/src/main/resources/static/

# 2. 后端打包
cd ../backend && mvn clean package
# 产物：backend/target/jsa.jar

# 3. 在 Windows 上（已装 JDK17 + MySQL 并执行 init.sql）
java -jar jsa.jar
# 浏览器访问 http://localhost:8080
```
