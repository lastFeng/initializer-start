package com.welford.spring.boot.blog.initializerstart.mapper;

import com.welford.spring.boot.blog.initializerstart.domain.Hello;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/22
 */
@Mapper
public interface HelloMapper {
    void saveOrUpdateHello(@Param("hello") Hello hello);
    void deleteHelloById(@Param("id") Long id);
    Hello getHelloById(@Param("id") Long id);
    List<Hello> getListHello();
}
