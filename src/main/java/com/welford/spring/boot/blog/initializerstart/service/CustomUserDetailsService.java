package com.welford.spring.boot.blog.initializerstart.service;

import com.welford.spring.boot.blog.initializerstart.domain.Hello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/23
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private HelloService helloService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Hello hello = helloService.getHelloByName(username);

        if (hello == null) {
            throw new UsernameNotFoundException(username);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        // 这边需要对应的用户+角色+权限表 TODO
//        for (SysRole role : user.getRoleList()) {
//            for (SysPermission permission: role.getPermission()) {
//                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
//            }
//        }
//        return new User(user.getUsername(), user.getPassword(), authorities);
        return new User(hello.getName(), hello.getEmail(), authorities);
    }
}
