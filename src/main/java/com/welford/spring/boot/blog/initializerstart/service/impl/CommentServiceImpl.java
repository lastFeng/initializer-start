package com.welford.spring.boot.blog.initializerstart.service.impl;

import com.welford.spring.boot.blog.initializerstart.domain.Comment;
import com.welford.spring.boot.blog.initializerstart.mapper.CommentReposity;
import com.welford.spring.boot.blog.initializerstart.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentReposity commentReposity;

    @Override
    public Comment getCommentById(Long id) {
        return commentReposity.getOne(id);
    }

    @Override
    public void removeComment(Long id) {
        commentReposity.deleteById(id);
    }
}
