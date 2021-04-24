package com.welford.spring.boot.blog.initializerstart.controller;

import com.welford.spring.boot.blog.initializerstart.domain.ElasticsearchBlog;
import com.welford.spring.boot.blog.initializerstart.utils.elasticsearch.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/blogs")
public class BlogController {
//    @Autowired
//    private ElasticsearchService elasticsearchService;


//    @GetMapping()
//    public List<Map<String, Object>> getBlogByIndexAndId(@RequestParam("index") String index,
//                                                         @RequestParam("id") String id) {
//        try {
//            return elasticsearchService.queryDocsByIds(new String[]{index}, new String[]{id}, null, 10);
//        } catch (IOException e) {
//            return null;
//        }
//    }

    @GetMapping
    public String listBlogs(@RequestParam(value="order",required=false,defaultValue="new") String order,
                            @RequestParam(value="tag",required=false) Long tag) {
        System.out.print("order:" +order + ";tag:" +tag );
        return "redirect:/index?order="+order+"&tag="+tag;
    }
}
