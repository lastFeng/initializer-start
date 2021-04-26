package com.welford.spring.boot.blog.initializerstart.service;

import com.welford.spring.boot.blog.initializerstart.domain.EsBlog;
import com.welford.spring.boot.blog.initializerstart.domain.User;
import com.welford.spring.boot.blog.initializerstart.domain.vo.TagVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public interface EsBlogService {
    void removeEsBlog(String id);
    EsBlog updateEsBlog(EsBlog esBlog);
    EsBlog getEsBlogsByBlogId(Long blogId);
    Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable);
    Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable);
    Page<EsBlog> listEsBlogs(Pageable pageable);
    List<EsBlog> listTop5NewestEsBlogs();
    List<EsBlog> listTop5HotestEsBlogs();
    List<TagVO> listTop30Tags();
    List<User> listTop12Users();
}
