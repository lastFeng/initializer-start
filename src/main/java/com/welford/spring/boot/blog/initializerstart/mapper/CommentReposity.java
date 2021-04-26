package com.welford.spring.boot.blog.initializerstart.mapper;

import com.welford.spring.boot.blog.initializerstart.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public interface CommentReposity extends JpaRepository<Comment, Long> {

}
