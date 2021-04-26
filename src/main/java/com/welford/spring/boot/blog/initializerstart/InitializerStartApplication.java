package com.welford.spring.boot.blog.initializerstart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@MapperScan("com.welford.spring.boot.blog.initializerstart.mapper")
public class InitializerStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(InitializerStartApplication.class, args);
    }

}
