package com.welford.spring.boot.blog.initializerstart.controller;

import com.google.common.collect.Lists;
import com.welford.spring.boot.blog.initializerstart.domain.Authority;
import com.welford.spring.boot.blog.initializerstart.domain.User;
import com.welford.spring.boot.blog.initializerstart.domain.vo.Response;
import com.welford.spring.boot.blog.initializerstart.service.AuthorityService;
import com.welford.spring.boot.blog.initializerstart.service.UserService;
import com.welford.spring.boot.blog.initializerstart.utils.exception.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/24
 */
@RestController
@RequestMapping("/users")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping
    public ModelAndView getUserList(@RequestParam(value = "async", required = false) boolean async,
                                    @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                    @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                    Model model) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<User> page = userService.listUsersByNameLike(name, pageable);
        List<User> list = page.getContent();

        model.addAttribute("page", page);
        model.addAttribute("userList", list);
        String url = async ? "users/list :: #mainContainerRepleace":"users/list";
        return new ModelAndView(url, "userModel", model);
    }

    @GetMapping("/add")
    public ModelAndView createForm(Model model) {
        model.addAttribute("user", new User(null, null, null, null));
        return new ModelAndView("users/add", "userModel", model);
    }

    @PostMapping()
    public ResponseEntity<Response> createUser(User user, Long authorityId) {
        List<Authority> authorities = Lists.newArrayList();
        authorities.add(authorityService.getAuthorityById(authorityId));
        user.setAuthorities(authorities);

        if (user.getId() == null) {
            user.setEncodePassword(user.getPassword());
        } else {
            User orgin = userService.getUserById(user.getId());
            String rawPassword = orgin.getPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            String enPassword = encoder.encode(user.getPassword());
            if (encoder.matches(rawPassword, enPassword)) {
                user.setPassword(user.getPassword());
            } else {
                user.setEncodePassword(user.getPassword());
            }
        }

        try {
            userService.saveUser(user);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }

        return ResponseEntity.ok(new Response(true, "处理成功", user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {
        try {
            userService.removeUser(id);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }
        return ResponseEntity.ok(new Response(true, "处理成功"));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView modifyUser(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return new ModelAndView("users/edit", "userModel", model);
    }
}
