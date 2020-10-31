package com.example.demo.service;

import com.example.demo.bean.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    public static User getCurrentUser(){
        // 返回的是Object类型
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
