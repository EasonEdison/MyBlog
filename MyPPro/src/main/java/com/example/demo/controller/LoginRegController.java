package com.example.demo.controller;

import com.example.demo.bean.ResponBean;
import com.example.demo.bean.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginRegController {

    @Autowired
    UserService userService;

    // 看看这个是在哪显示的，奥，前端管的
    // 登录失败页面
    @RequestMapping("/login_error")
    public ResponBean loginError(){
        return new ResponBean("error", "登录失败！");
    }

    // 登录成功页面
    @RequestMapping("/login_success")
    public ResponBean loginSuccess(){
        return new ResponBean("success", "登录成功！");
    }

    // 这个是还没登录，但是输入登录后的URI显示的吗？
    // 未登录页面
    @RequestMapping("/login_page")
    public ResponBean loginPage(){
        return new ResponBean("error", "尚未登录，请登录！");
    }

    // 还没有创建功能
    // 创建，用Post
    @PostMapping("/reg")
    public ResponBean reg(User user){
        int result = userService.InsertUser(user);
        if (result == 0) {
            //成功
            return new ResponBean("success", "注册成功!");
        } else if (result == 1) {
            return new ResponBean("error", "用户名重复，注册失败!");
        } else {
            //失败
            return new ResponBean("error", "注册失败!");
        }
    }
}
