package com.welford.spring.boot.blog.initializerstart.service;

import com.welford.spring.boot.blog.initializerstart.domain.Catalog;
import com.welford.spring.boot.blog.initializerstart.domain.User;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public interface CatalogService {
    Catalog saveCatalog(Catalog catalog);

    void removeCatalog(Long id);

    Catalog getCatalogById(Long id);

    List<Catalog> listCatalogs(User user);
}
