package com.welford.spring.boot.blog.initializerstart.service.impl;

import com.welford.spring.boot.blog.initializerstart.domain.EsBlog;
import com.welford.spring.boot.blog.initializerstart.domain.User;
import com.welford.spring.boot.blog.initializerstart.domain.vo.TagVO;
import com.welford.spring.boot.blog.initializerstart.mapper.EsBlogRepository;
import com.welford.spring.boot.blog.initializerstart.service.EsBlogService;
import com.welford.spring.boot.blog.initializerstart.service.UserService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/25
 */
@Service
public class EsBlogServiceImpl implements EsBlogService {

    @Autowired
    private EsBlogRepository esBlogRepository;

//    @Autowired
//    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static final Pageable TOP_5_PAGEABLE = PageRequest.of(0, 5);
    private static final String EMPTY_KEYWORD = "";

    @Override
    public void removeEsBlog(String id) {
        esBlogRepository.deleteById(id);
    }

    @Override
    public EsBlog updateEsBlog(EsBlog esBlog) {
        return esBlogRepository.save(esBlog);
    }

    @Override
    public EsBlog getEsBlogsByBlogId(Long blogId) {
        return esBlogRepository.findEsBlogByBlogId(blogId);
    }

    @Override
    public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        if (pageable.getSort() == null) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        return esBlogRepository.findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
    }

    @Override
    public Page<EsBlog> listHotestEsBlogs(String keyword, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC,"readSize","commentSize","voteSize","createTime");
        if (pageable.getSort() == null) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        return esBlogRepository.findDistinctByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
    }

    @Override
    public Page<EsBlog> listEsBlogs(Pageable pageable) {
        return esBlogRepository.findAll(pageable);
    }

    @Override
    public List<EsBlog> listTop5NewestEsBlogs() {
        return this.listNewestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE).getContent();
    }

    @Override
    public List<EsBlog> listTop5HotestEsBlogs() {
        return this.listHotestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE).getContent();
    }

    @Override
    public List<TagVO> listTop30Tags() {
        List<TagVO> result = new ArrayList<>();

//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.size(30);
//        QueryBuilders.matchAllQuery()
//        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchAllQuery())
//                .withSearchType(SearchType.QUERY_THEN_FETCH)
//                .addAggregation(AggregationBuilders.terms("tags").field("tags"));
//        sourceBuilder.query(queryBuilder);

        // TODO:这里要重写
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(30);
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder aggregations = AggregationBuilders.terms("tags")
                .field("tags").order(BucketOrder.count(false));
        sourceBuilder.aggregation(aggregations);

        SearchRequest request = new SearchRequest("blog");
        request.source(sourceBuilder);
        SearchResponse response = null;

        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringTerms modelTerms = (StringTerms) response.getAggregations().getAsMap().get("tags");
        Iterator<StringTerms.Bucket> iterator = modelTerms.getBuckets().iterator();
        while (iterator.hasNext()) {
            StringTerms.Bucket next = iterator.next();
            result.add(new TagVO(next.getKeyAsString(), next.getDocCount()));
        }
        return result;
    }

    @Override
    public List<User> listTop12Users() {
        List<String> usernameList = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(12);
        sourceBuilder.query(QueryBuilders.matchAllQuery());
        TermsAggregationBuilder aggregations = AggregationBuilders.terms("users")
                .field("username").order(BucketOrder.count(false));
        sourceBuilder.aggregation(aggregations);

        SearchRequest request = new SearchRequest("blog");
        request.source(sourceBuilder);
        SearchResponse response = null;

        try {
            response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringTerms modelTerms = (StringTerms) response.getAggregations().getAsMap().get("tags");
        Iterator<StringTerms.Bucket> iterator = modelTerms.getBuckets().iterator();
        while (iterator.hasNext()) {
            StringTerms.Bucket next = iterator.next();
            usernameList.add(next.getKeyAsString());
        }
        return userService.listUsersByUsernames(usernameList);
    }
}
