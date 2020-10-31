package com.example.demo.controller.admin;

import com.example.demo.bean.ResponBean;
import com.example.demo.bean.Role;
import com.example.demo.bean.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserManaController {
    @Autowired
    UserService userService;

    // 通过昵称搜索用户，前端UserMana页面的搜索，模糊查询
    // 前端：getRequest("/admin/user?nickname="+this.keywords)
    // ?被直接接收为参数了
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> getUserByNickname(String nickname) {
        // 打印一下，看id还在不在不
        System.out.println("查看搜索完整性");
        for (User  user : userService.getUserByNickName(nickname)){
            System.out.println(user.toString());
        }
        return userService.getUserByNickName(nickname);
    }

    // 只加载一个
    // 前端：("/admin/user/" + id)
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // 删除
    @RequestMapping(value = "/user/{uid}", method = RequestMethod.DELETE)
    public ResponBean deleteUserById(@PathVariable Long uid) {
        if (userService.deleteUserById(uid) == 1){
            return new ResponBean("success", "删除成功！");
        } else {
            return new ResponBean("error", "删除失败！");
        }
    }

    // 获取全部角色, 不只是用户子集的，是角色表里的全部
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    public List<Role> getAllRole(){
        return userService.getAllRole();
    }

    // 更改用户的可用性
    // 前端：putRequest("/admin/user/enabled", {enabled: enabled, uid: id})
    @RequestMapping(value = "/user/enabled", method = RequestMethod.PUT)
    public ResponBean updateUserEnabled(Boolean enabled, Long uid){
        if (userService.updateUserEnabled(enabled, uid) == 1){
            return new ResponBean("success", "更新成功！");
        } else {
            return new ResponBean("error", "更新失败！");
        }
    }

    // 更改用户角色
    // 前端：putRequest("/admin/user/role", {rids: this.roles, id: id})
    // 前面的参数是个数组
    @RequestMapping(value = "/user/role", method = RequestMethod.PUT)
    public ResponBean updateUserRoles(Long[] rids, Long id){
        System.out.println("设置用户角色id也没了吗？ " + id);
        // 注意，返回的数字应是role个数
        if (userService.updateUserRoles(rids, id) == rids.length){
            return new ResponBean("success", "更新成功！");
        } else {
            return new ResponBean("error", "更新失败！");
        }
    }

}
