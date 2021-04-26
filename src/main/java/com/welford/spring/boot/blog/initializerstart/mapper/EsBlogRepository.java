package com.welford.spring.boot.blog.initializerstart.mapper;

import com.welford.spring.boot.blog.initializerstart.domain.EsBlog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
public interface EsBlogRepository extends ElasticsearchRepository<EsBlog, String> {
    Page<EsBlog> findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(String title,
                                                                                                     String summary,
                                                                                                     String content,
                                                                                                     String tags,
                                                                                                     Pageable pageable);

    EsBlog findEsBlogByBlogId(Long blogId);
}
