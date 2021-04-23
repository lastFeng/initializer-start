package com.welford.spring.boot.blog.initializerstart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : guoweifeng
 * @date : 2021/4/23
 */
@Controller
@RequestMapping
public class IndexController {
//    @GetMapping(value = {"/", "/index"})
//    public String index() {
//        return "index";
//    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

//    @GetMapping("/loginError")
//    public String loginError(Model model) {
//        model.addAttribute("loginError", true);
//        model.addAttribute("errorMsg", "登录失败，用户名或密码错误");
//        return "/login";
//    }
}
