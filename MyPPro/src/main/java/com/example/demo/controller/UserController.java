package com.example.demo.controller;

import com.example.demo.bean.ResponBean;
import com.example.demo.service.UserService;
import com.example.demo.service.Utils;
import org.apache.el.parser.BooleanNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    // 三个基本的查询操作, 登录的时候前端钩子函数会触发一部分
    @RequestMapping("/currentUserName")
    public String currentUserName(){
        return Utils.getCurrentUser().getNickname();
    }

    @RequestMapping("/currentUserId")
    public Long currentUserId(){
        return Utils.getCurrentUser().getId();
    }

    @RequestMapping("/currentUserEmail")
    public String currentUserEmail(){
        return Utils.getCurrentUser().getEmail();
    }

    // 验证权限，是否是超级管理员，点击 “博客管理” 的时候，会判断，是超级管理员的时候，才能进入
    @RequestMapping("/isAdmin")
    public Boolean isAdmin(){
        // 直接调用实体类的获取权限方法
        List<GrantedAuthority> authorities = Utils.getCurrentUser().getAuthorities();
        // 然后遍历，看有没有对的上的
        for (GrantedAuthority authority : authorities){
            // 获取的是ROLE_超级管理员，所以检测子串
            if (authority.getAuthority().contains("超级管理员")){
                return true;
            }
        }
        return false;
    }

    // 修改邮箱, Restful风格
    @RequestMapping(value = "/updateUserEmail", method = RequestMethod.PUT)
    public ResponBean updateUserEmail(String email){
        if (userService.updateUserEmail(email) == 1){
            return new ResponBean("success", "修改成功！");
        }
        return new ResponBean("error", "修改失败！");
    }

}
