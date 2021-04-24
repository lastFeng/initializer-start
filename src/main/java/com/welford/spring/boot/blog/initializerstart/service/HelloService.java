//package com.welford.spring.boot.blog.initializerstart.service;
//
//import com.welford.spring.boot.blog.initializerstart.domain.Hello;
//import com.welford.spring.boot.blog.initializerstart.mapper.HelloMapper;
//import org.hibernate.internal.HEMLogging;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * @author : guoweifeng
// * @date : 2021/4/22
// */
//@Service
//public class HelloService {
//    @Autowired
//    private HelloMapper helloMapper;
//
//    public List<Hello> getListHello() {
//        return helloMapper.getListHello();
//    }
//
//    public Hello getHelloById(Long id) {
//        return helloMapper.getHelloById(id);
//    }
//
//    public Hello saveOrUpdateHello(Hello hello) {
//        if (hello.getId() != null) {
//            helloMapper.deleteHelloById(hello.getId());
//        }
//        helloMapper.saveOrUpdateHello(hello);
//
//        return hello;
//    }
//
//    public void deleteHelloById(Long id) {
//        helloMapper.deleteHelloById(id);
//    }
//
//    public Hello getHelloByName(String name) {
//        return helloMapper.getHelloByName(name);
//    }
//}
