package com.welford.spring.boot.blog.initializerstart.mapper;

import com.welford.spring.boot.blog.initializerstart.domain.Blog;
import com.welford.spring.boot.blog.initializerstart.domain.Catalog;
import com.welford.spring.boot.blog.initializerstart.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : guoweifeng
 * @date : 2021/4/24
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);

    Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

    Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title, User user,
                                                                            String tags, User user2, Pageable pageable);
    Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
}
