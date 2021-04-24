package com.welford.spring.boot.blog.initializerstart.mapper;

import com.welford.spring.boot.blog.initializerstart.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : guoweifeng
 * @date : 2021/4/24
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByNameLike(String name, Pageable pageable);

    User findByUsername(String username);
}
