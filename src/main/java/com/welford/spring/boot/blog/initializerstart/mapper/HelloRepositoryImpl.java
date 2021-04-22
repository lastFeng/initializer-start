//package com.welford.spring.boot.blog.initializerstart.repository;
//
//import com.welford.spring.boot.blog.initializerstart.domain.Hello;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicLong;
//
///**
// * @author : guoweifeng
// * @date : 2021/4/22
// */
////@Repository
//public class HelloRepositoryImpl implements HelloRepository{
//    private static final ConcurrentHashMap<Long, Hello> helloMap = new ConcurrentHashMap<>();
//    private static AtomicLong count = new AtomicLong(1L);
//
//    @Override
//    public Hello saveOrUpdateHello(Hello hello) {
//
//        if (hello == null) {
//            hello = new Hello(count.incrementAndGet(), "", "");
//        }
//
//        if (hello.getId() == null) {
//            hello.setId(count.incrementAndGet());
//        }
//        helloMap.put(hello.getId(), hello);
//        return hello;
//    }
//
//    @Override
//    public void deleteHelloById(Long id) {
//        if (helloMap.get(id) != null) {
//            helloMap.remove(id);
//        }
//    }
//
//    @Override
//    public Hello getHelloById(Long id) {
//        return helloMap.get(id);
//    }
//
//    @Override
//    public List<Hello> getListHello() {
//        return new ArrayList(helloMap.values());
//    }
//}
