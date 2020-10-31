package com.example.demo.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class MyAuthenProvider implements AuthenticationProvider {

    // 验证方法
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 只要用户名是这个，不管什么密码，都可以变成超级用户
        if (name.equals("卢本伟")){
            // 创造权限集合, 跟UserDetails中的方法差不多
            // 用list应该没问题
            List<GrantedAuthority> authorityCollection = new ArrayList<>();
            authorityCollection.add(new SimpleGrantedAuthority("超级管理员"));
            authorityCollection.add(new SimpleGrantedAuthority("普通用户"));
            // 第一个参数是principal(权限)，第二个是密码，第三个是额外加一些权限
            return new UsernamePasswordAuthenticationToken("超级管理员",password, authorityCollection);
        } else {
            return null;
        }


    }

    // 检查是不是这个验证器支持的方法
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
