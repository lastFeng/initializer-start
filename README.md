### blog project
#### Project Environment
- idea 2021.01
- jdk 1.8+
- gradle 6.8.3
- mysql 5.7+
- elasticsearch 7.12.0

#### build project
- download
- gradle build
- properties modify: src/main/resources/application.properties[elasticsearch.ip, spring.datasource.url, spring.datasource.username, spring.datasource.password]
- run the sql script in MySQL database: src/main/resources/sql/init.sql
- idea - com/welford/spring/boot/blog/initializerstart/InitializerStartApplication.java run

#### 核心功能
- 用户管理
  - 注册
  - 登录
  - 修改
  - 删除
  - 搜索
  - 注销
  - ...
- 安全设置
  - 授权
  - 权限管理
  - ...  
- 博客管理
  - 发表
  - 编辑
  - 删除
  - 分类
  - 设置标签
  - 图片上传
  - 模糊查询
- 评论管理
  - 发表
  - 删除
  - 统计
- 点赞管理
  - 点赞
  - 删除
  - 统计
- 分类管理
  - 创建
  - 编辑
  - 删除
  - 查询
- 标签管理
  - 创建
  - 编辑
  - 删除
  - 查询
- 首页搜索
  - 全文检索
  - 排序
    - 最新
    - 最热
  - 热门标签
  - 热门用户

#### 模块
##### 权限管理
- RBAC(Role-Based Access Control)
    - 隐式访问控制
    - 显示访问控制
- 解决方案
    - Shiro
    - Spring Security
###### Spring Security
- 核心领域概念
    - 认证(authentication)
    - 授权(authorization)
- 身份验证技术
    - HTTP BASIC
    - HTTP Digest
    - HTTP X.509
    - LDAP
    - 基于表单的认证
    - OpenId
- 模块
    - Core
    - Remoting
    - Web
    - Config
    - LDAP
    - ACL
    - CAS
    - OpenId
    - Test