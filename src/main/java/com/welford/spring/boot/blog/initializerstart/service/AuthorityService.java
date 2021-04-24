package com.welford.spring.boot.blog.initializerstart.service;

import com.welford.spring.boot.blog.initializerstart.domain.Authority;

/**
 * @author : guoweifeng
 * @date : 2021/4/24
 */
public interface AuthorityService {
    Authority getAuthorityById(Long id);
}
