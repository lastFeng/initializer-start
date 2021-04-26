package com.welford.spring.boot.blog.initializerstart.controller;

import com.welford.spring.boot.blog.initializerstart.domain.ElasticsearchBlog;
import com.welford.spring.boot.blog.initializerstart.domain.EsBlog;
import com.welford.spring.boot.blog.initializerstart.domain.User;
import com.welford.spring.boot.blog.initializerstart.domain.vo.TagVO;
import com.welford.spring.boot.blog.initializerstart.service.EsBlogService;
import com.welford.spring.boot.blog.initializerstart.utils.elasticsearch.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
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

//    @GetMapping
//    public String listBlogs(@RequestParam(value="order",required=false,defaultValue="new") String order,
//                            @RequestParam(value="tag",required=false) Long tag) {
//        System.out.print("order:" +order + ";tag:" +tag );
//        return "redirect:/index?order="+order+"&tag="+tag;
//    }
    @Autowired
    private EsBlogService esBlogService;

    @GetMapping
    public String listBlog(@RequestParam(value = "order", required = false, defaultValue = "new") String order,
                           @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                           @RequestParam(value = "async", required = false) boolean async,
                           @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                           Model model) {
        Page<EsBlog> page = null;
        boolean isEmpty = true;

        try {
            if (order.equals("hot")) {
                Sort sort = Sort.by(Sort.Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
                Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
                page = esBlogService.listHotestEsBlogs(keyword, pageable);
            } else if (order.equals("new")) {
                Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
                Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
                page = esBlogService.listNewestEsBlogs(keyword, pageable);
            }
            isEmpty = false;
        } catch (Exception e) {
            Sort sort;
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            page = esBlogService.listEsBlogs(pageable);
        }

        List<EsBlog> list = page.getContent();
        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);

        if (!async && !isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            List<EsBlog> hostest = esBlogService.listTop5HotestEsBlogs();
            List<User> users = esBlogService.listTop12Users();
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("newest", newest);
            model.addAttribute("hotest", hostest);
            model.addAttribute("tags", tags);
            model.addAttribute("users", users);
        }

        return async ? "/index :: #mainContainerReplace" : "/index";
    }

    @GetMapping("/newest")
    public String listNewestEsBlogs(Model model) {
        model.addAttribute("newest", esBlogService.listTop5NewestEsBlogs());
        return "/newest";
    }

    @GetMapping("/hotest")
    public String listHotestEsBlogs(Model model) {
        model.addAttribute("hotest", esBlogService.listTop5HotestEsBlogs());
        return "/hotest";
    }

}
