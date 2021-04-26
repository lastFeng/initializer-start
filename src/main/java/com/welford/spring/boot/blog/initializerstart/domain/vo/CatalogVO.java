package com.welford.spring.boot.blog.initializerstart.domain.vo;

import com.welford.spring.boot.blog.initializerstart.domain.Catalog;

import java.io.Serializable;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public class CatalogVO implements Serializable {
    private String username;
    private Catalog catalog;

    public CatalogVO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
