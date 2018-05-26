
-- 创建数据库并授权
create database if not EXISTS liuns DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER ON liuns.* TO liuns@127.0.0.1 IDENTIFIED BY 'root';

-- 创建用户表
use liuns;
CREATE TABLE IF NOT EXISTS user (
  id bigint PRIMARY KEY ,
  username VARCHAR(50) not null comment '登录用户名',
  pwd VARCHAR(50) comment '登录密码',
  is_deleted bigint comment '逻辑删除：0-正常 1-已删除',
  create_user_id bigint comment '创建者id',
  create_time TIMESTAMP default CURRENT_TIMESTAMP comment '创建时间',
  last_update_user_id bigint comment '最后更改者id',
  last_update_time TIMESTAMP default CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP comment '最后更改时间',
  server_ip VARCHAR(32) comment '服务端ip',
  version_no VARCHAR (10) comment '版本号'
) comment '用户' ENGINE=InnoDB;

-- 创建地区表
use liuns;
CREATE TABLE IF NOT EXISTS address (
  id bigint PRIMARY KEY ,
  parent_id bigint not null comment '父级地区id',
  address_code VARCHAR(50) comment '地区代码',
  address_name VARCHAR (100) comment '地区名称',
  is_deleted bigint comment '逻辑删除：0-正常 1-已删除',
  create_user_id bigint comment '创建者id',
  create_time TIMESTAMP default CURRENT_TIMESTAMP comment '创建时间',
  last_update_user_id bigint comment '最后更改者id',
  last_update_time TIMESTAMP default CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP comment '最后更改时间',
  server_ip VARCHAR(32) comment '服务端ip',
  version_no VARCHAR (10) comment '版本号'
) comment '地区' ENGINE=InnoDB;
