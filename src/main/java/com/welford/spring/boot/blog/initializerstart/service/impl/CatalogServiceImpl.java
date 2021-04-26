package com.welford.spring.boot.blog.initializerstart.service.impl;

import com.welford.spring.boot.blog.initializerstart.domain.Catalog;
import com.welford.spring.boot.blog.initializerstart.domain.User;
import com.welford.spring.boot.blog.initializerstart.mapper.CatalogRepository;
import com.welford.spring.boot.blog.initializerstart.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private CatalogRepository catalogRepository;

    @Override
    public Catalog saveCatalog(Catalog catalog) {
        List<Catalog> byUserAndName = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());

        if (byUserAndName != null && byUserAndName.size() > 0) {
            throw new IllegalArgumentException("该分类已存在！");
        }
        return catalogRepository.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    @Override
    public Catalog getCatalogById(Long id) {
        return catalogRepository.findById(id).get();
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }
}
