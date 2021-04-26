package com.welford.spring.boot.blog.initializerstart.service;

import com.welford.spring.boot.blog.initializerstart.domain.Comment;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public interface CommentService {

    Comment getCommentById(Long id);

    void removeComment(Long id);
}
