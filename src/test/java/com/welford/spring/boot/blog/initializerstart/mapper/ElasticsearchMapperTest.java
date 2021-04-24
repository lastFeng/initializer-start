//package com.welford.spring.boot.blog.initializerstart.mapper;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.welford.spring.boot.blog.initializerstart.domain.ElasticsearchBlog;
//import com.welford.spring.boot.blog.initializerstart.utils.elasticsearch.ElasticsearchService;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.IOException;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class ElasticsearchMapperTest {
//
//    @Autowired
//    private ElasticsearchBlogMapper blogMapper;
//
//    /***
//     * ES版本 - 7+（最好是最新的）
//     */
//    @Autowired
//    private ElasticsearchService elasticsearchService;
//
//    @BeforeAll
//    public void testInitRepositoryData() throws IOException {
//        blogMapper.deleteAll();
//        ElasticsearchBlog test = new ElasticsearchBlog("title", "summary", "content");
////        ElasticsearchBlog save = blogMapper.save(test);
////        Assertions.assertNotNull(save);
//        ObjectMapper objectMapper = new ObjectMapper();
//        boolean blog = elasticsearchService.insertData("blog", "1234", objectMapper.writeValueAsString(test));
//        Assertions.assertTrue(blog);
//    }
//
//    @Test
//    public void testFind() throws IOException {
//        Iterable<ElasticsearchBlog> all = blogMapper.findAll();
//        Assertions.assertNotNull(all);
//        long count = elasticsearchService.count(new String[]{"blog"});
//        Assertions.assertEquals(1, count);
//    }
//
//}
