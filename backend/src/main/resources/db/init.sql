-- ============================================================
-- 健身打卡系统（JSA）数据库初始化脚本
-- 见 docs/03 数据库设计。语法兼容 MySQL 8.0+（见 docs/06）。
-- 用法（Windows 演示机）：用 MySQL 客户端执行本脚本，建库建表 + 测试数据。
-- ============================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS jsa DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE jsa;

-- 为可重复执行，先按外键依赖顺序删表
DROP TABLE IF EXISTS checkin_record;
DROP TABLE IF EXISTS sport;
DROP TABLE IF EXISTS user;

-- 用户表
CREATE TABLE user (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  username    VARCHAR(50)  NOT NULL,
  password    VARCHAR(100) NOT NULL,
  nickname    VARCHAR(50)  NOT NULL,
  role        VARCHAR(20)  NOT NULL DEFAULT 'USER',
  create_time DATETIME     NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 运动项目表
CREATE TABLE sport (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  name        VARCHAR(50)  NOT NULL,
  description VARCHAR(200) NULL,
  create_time DATETIME     NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_sport_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 打卡记录表
CREATE TABLE checkin_record (
  id           BIGINT       NOT NULL AUTO_INCREMENT,
  user_id      BIGINT       NOT NULL,
  sport_id     BIGINT       NOT NULL,
  content      VARCHAR(500) NOT NULL,
  status       VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
  checkin_time DATETIME     NOT NULL,
  reviewer_id  BIGINT       NULL,
  review_time  DATETIME     NULL,
  PRIMARY KEY (id),
  KEY idx_record_user (user_id),
  KEY idx_record_sport (sport_id),
  CONSTRAINT fk_record_user     FOREIGN KEY (user_id)     REFERENCES user(id),
  CONSTRAINT fk_record_sport    FOREIGN KEY (sport_id)    REFERENCES sport(id),
  CONSTRAINT fk_record_reviewer FOREIGN KEY (reviewer_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始化数据：用户（演示密码明文，便于讲解；见 docs/03 备注）
INSERT INTO user (username, password, nickname, role, create_time) VALUES
  ('admin', '123456', '系统管理员', 'ADMIN', NOW()),
  ('alice', '123456', '爱丽丝',     'USER',  NOW()),
  ('bob',   '123456', '鲍勃',       'USER',  NOW());

-- 初始化数据：运动项目
INSERT INTO sport (name, description, create_time) VALUES
  ('跑步',     '有氧运动，建议每次 30 分钟以上', NOW()),
  ('游泳',     '全身性有氧运动',                 NOW()),
  ('力量训练', '哑铃、杠铃等器械训练',           NOW()),
  ('瑜伽',     '柔韧性与核心训练',               NOW()),
  ('骑行',     '室内外有氧骑行',                 NOW());

-- 初始化数据：打卡记录（覆盖三种审核状态）
INSERT INTO checkin_record (user_id, sport_id, content, status, checkin_time, reviewer_id, review_time) VALUES
  (2, 1, '晨跑 5 公里，状态不错', 'APPROVED', NOW(), 1, NOW()),
  (2, 3, '练了胸和背',           'PENDING',  NOW(), NULL, NULL),
  (3, 2, '游了 1000 米',          'PENDING',  NOW(), NULL, NULL),
  (3, 4, '瑜伽放松 40 分钟',     'REJECTED', NOW(), 1, NOW());
