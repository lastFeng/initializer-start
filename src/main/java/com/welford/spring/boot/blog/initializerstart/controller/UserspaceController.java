package com.welford.spring.boot.blog.initializerstart.controller;

import com.welford.spring.boot.blog.initializerstart.domain.Blog;
import com.welford.spring.boot.blog.initializerstart.domain.Catalog;
import com.welford.spring.boot.blog.initializerstart.domain.User;
import com.welford.spring.boot.blog.initializerstart.domain.Vote;
import com.welford.spring.boot.blog.initializerstart.domain.vo.Response;
import com.welford.spring.boot.blog.initializerstart.service.BlogService;
import com.welford.spring.boot.blog.initializerstart.service.CatalogService;
import com.welford.spring.boot.blog.initializerstart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

/**
 * @author : guoweifeng
 * @date : 2021/4/23
 * 用户个人信息页面管理
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {
    @Qualifier("userServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    /**
     * 通过用户名获取用户信息与用户博客
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + username + "/blogs";
    }

    /**
     * 获取用户信息
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/profile", "userModel", model);
    }

    /**
     * 修改用户信息
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username, User user) {
        User originUser = userService.getUserById(user.getId());
        originUser.setEmail(user.getEmail());
        originUser.setName(user.getName());

        String rawPassword = originUser.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String enPassword = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword, enPassword);
        if (!isMatch) {
            originUser.setEncodePassword(user.getPassword());
        }
        userService.saveUser(originUser);
        return "redirect:/u/" + username + "/profile";
    }

    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username, Model model) {
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/actavar", "userModel", model);
    }

    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username,
                                               @RequestBody User user) {
        String avatarUrl = user.getAvatar();
        User origin = userService.getUserById(user.getId());
        origin.setAvatar(avatarUrl);
        userService.saveUser(origin);

        return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
    }

    /***
     * 获取用户的博客
     * @param username
     * @param order
     * @param category
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order", required = false, defaultValue = "new") String order,
                                   @RequestParam(value = "category", required = false) Long category,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                   @RequestParam(value = "async", required = false) boolean async,
                                   @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                   Model model) {
        User user = (User) userService.getUserByUsername(username);
        model.addAttribute("user", user);

        if (category != null) {
            return "/u";
        }

        Page<Blog> page = null;
        if (category != null && category > 0) {
            Catalog catalog = catalogService.getCatalogById(category);
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            page = blogService.listBlogsByCatalog(catalog, pageable);
            order = "";
        }
        if (order.equals("hot")) {
            Sort sort = Sort.by(Sort.Direction.DESC, "reading", "comments", "likes");
            Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleLikeAndSort(user, keyword, pageable);
        }

        if (order.equals("new")) {
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            page = blogService.listBlogsByTitleLike(user, keyword, pageable);
        }

        List<Blog> list = page.getContent();
        model.addAttribute("user", user);
        model.addAttribute("catalogId", category);
        model.addAttribute("keyword", keyword);
        model.addAttribute("order", order);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return async ? "/userspace/u :: #mainContainerRepleace" :"/userspace/u";
    }

    @GetMapping("/{username}/blogs/{id}")
    public String getBlogsById(@PathVariable("username") String username,
                               @PathVariable("id") Long id, Model model) {

        User principal = null;
        Blog blog = blogService.getBlogById(id);
        blogService.readingIncrease(id);

        boolean isBlogOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && Objects.equals(username, principal.getUsername())) {
                isBlogOwner = true;
            }
        }

        List<Vote> votes = blog.getVotes();
        Vote currentVote = null;
        if (principal != null) {
            for (Vote vote : votes) {
                if (Objects.equals(principal.getUsername(), vote.getUser().getUsername())) {
                    currentVote = vote;
                    break;
                }
            }
        }

        model.addAttribute("isBlogOwner", isBlogOwner);
        model.addAttribute("blogModel", blogService.getBlogById(id));
        model.addAttribute("currentVote", currentVote);

        return "/userspace/blog";
    }

    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,
                                               @PathVariable("id") Long id) {
        try {
            blogService.removeBlog(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", "/u/" + username + "/blogs"));
    }

    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(@PathVariable("username")String username, Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("catalogs", catalogs);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView editBlog(@PathVariable("username") String username,
                                 @PathVariable("id") Long id,
                                 Model model) {
        User user = (User) userDetailsService.loadUserByUsername(username);

        model.addAttribute("blog", blogService.getBlogById(id));
        model.addAttribute("catalogs",  catalogService.listCatalogs(user));
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    @PostMapping("/{username}/blogs/edit")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {

        if (blog.getCatalog().getId() == null) {
            return ResponseEntity.ok().body(new Response(false, "未选择分类"));
        }

        try {
            if (blog.getId() != null) {
                Blog origin = blogService.getBlogById(blog.getId());
                origin.setTitle(blog.getTitle());
                origin.setContent(blog.getContent());
                origin.setSummary(blog.getSummary());
                origin.setCatalog(blog.getCatalog());
                origin.setTags(blog.getTags());
                blogService.saveBlog(origin);
            } else {
                User user = (User) userDetailsService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }
}
