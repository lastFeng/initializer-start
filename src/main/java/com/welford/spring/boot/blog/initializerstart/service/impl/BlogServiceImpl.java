package com.welford.spring.boot.blog.initializerstart.service.impl;

import com.welford.spring.boot.blog.initializerstart.domain.*;
import com.welford.spring.boot.blog.initializerstart.mapper.BlogRepository;
import com.welford.spring.boot.blog.initializerstart.service.BlogService;
import com.welford.spring.boot.blog.initializerstart.service.EsBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author : guoweifeng
 * @date : 2021/4/24
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private EsBlogService esBlogService;

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        boolean isNew = blog.getId() == null;
        Blog one = blogRepository.save(blog);
        EsBlog esBlog;
        if (isNew) {
            esBlog = new EsBlog(one);
        } else {
            esBlog = esBlogService.getEsBlogsByBlogId(blog.getId());
            esBlog.update(one);
        }
        esBlogService.updateEsBlog(esBlog);
        return one;
    }

    @Transactional
    @Override
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
        EsBlog esBlogsByBlogId = esBlogService.getEsBlogsByBlogId(id);
        esBlogService.removeEsBlog(esBlogsByBlogId.getId());
    }

    @Transactional
    @Override
    public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.findById(id).get();
    }

    @Override
    public Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, user, title, user, pageable);
        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable) {
        // 模糊查询
        title = "%" + title + "%";
        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.findById(id).get();
        blog.setReadSize(blog.getReadSize()+1);
        blogRepository.save(blog);
    }

    @Override
    public Page<Blog> listBlogsByTitleVote(User user, String title, Pageable pageable) {
        title = "%" + title + "%";
        return blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, user, title, user, pageable);
    }

    @Override
    public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
        return blogRepository.findByCatalog(catalog, pageable);
    }

    @Override
    public Blog createComment(Long blogId, String commentContent) {
        Blog blog = blogRepository.getOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = new Comment(commentContent, user);
        blog.addComment(comment);
        return this.saveBlog(blog);
    }

    @Override
    public void removeComment(Long blogId, Long commentId) {
        Blog blog = blogRepository.getOne(blogId);
        blog.removeComment(commentId);
        this.saveBlog(blog);
    }

    @Override
    public Blog createVote(Long blogId) {
        Blog blog = blogRepository.getOne(blogId);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Vote vote = new Vote(user);
        boolean add = blog.addVote(vote);
        if (!add) {
            throw new IllegalArgumentException("该用户已经点过赞！");
        }
        return this.saveBlog(blog);
    }

    @Override
    public void removeVote(Long blogId, Long voteId) {
        Blog blog = blogRepository.getOne(blogId);
        blog.removeVote(voteId);
        this.saveBlog(blog);
    }
}
