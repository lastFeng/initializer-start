package com.welford.spring.boot.blog.initializerstart.mapper;

import com.welford.spring.boot.blog.initializerstart.domain.Catalog;
import com.welford.spring.boot.blog.initializerstart.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {
    List<Catalog> findByUser(User user);

    List<Catalog> findByUserAndName(User user, String name);
}
