package com.welford.spring.boot.blog.initializerstart.controller;

import com.welford.spring.boot.blog.initializerstart.domain.Hello;
import com.welford.spring.boot.blog.initializerstart.mapper.HelloMapper;
import com.welford.spring.boot.blog.initializerstart.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author : guoweifeng
 * @date : 2021/4/22
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private HelloService helloService;

    @GetMapping
    public ModelAndView listHello(Model model) {
        model.addAttribute("helloList", helloService.getListHello());
        model.addAttribute("title", "用户列表");
        return new ModelAndView("hello/list", "hellModel", model);
    }

    @GetMapping("/{id}")
    public ModelAndView getHelloById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("hello", helloService.getHelloById(id));
        model.addAttribute("title", "查看用户");
        return new ModelAndView("hello/view", "hellModel", model);
    }

    @GetMapping("/form")
    public ModelAndView createForm(Model model) {
        model.addAttribute("hello", new Hello());
        model.addAttribute("title", "创建用户");
        return new ModelAndView("hello/form", "helloModel", model);
    }

    @PostMapping
    public ModelAndView saveOrUpdateHello(Hello hello, Model model) {
        hello = helloService.saveOrUpdateHello(hello);
        return new ModelAndView("redirect:/hello");
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteById(@PathVariable("id") Long id, Model model) {
        helloService.deleteHelloById(id);
        return new ModelAndView("redirect:/hello");
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateHelloById(@PathVariable("id")Long id, Model model) {
        Hello helloById = helloService.getHelloById(id);
        model.addAttribute("hello", helloById);
        model.addAttribute("title", "修改");
        return new ModelAndView("hello/form", "helloModel", model);
    }
}
