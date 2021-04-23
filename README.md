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
