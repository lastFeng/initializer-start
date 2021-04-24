-- 数据库
drop database if exists blog;
create database blog default character set utf8mb4 collate utf8mb4_bin;
use blog;
INSERT INTO user (id, username, password, name, email) VALUES (1, 'admin', '$2a$10$Of2gW7dGjwmTeYm3blcdpuHpVJLDZAy4LR7ShSAnjQbWO.978cZVC', 'admin', 'admin@admin.com');
INSERT INTO user (id, username, password, name, email)  VALUES (2, 'test', '$2a$10$Of2gW7dGjwmTeYm3blcdpuHpVJLDZAy4LR7ShSAnjQbWO.978cZVC', 'test', 'test@test.com');

INSERT INTO authority (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authority (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
