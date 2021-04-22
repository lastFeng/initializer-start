-- 数据库
drop database if exists blog;
create database blog default character set utf8mb4 collate utf8mb4_bin;
use blog;
-- 用户表
drop table if exists hello;
create table if not exists hello (
    id int(11) unsigned not null auto_increment,
    name varchar(64) not null default '',
    email varchar(128) not null default '',
    primary key(id)
)engine=InnoDB;