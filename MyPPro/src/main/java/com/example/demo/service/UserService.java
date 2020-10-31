package com.example.demo.service;


// 涉及到权限验证

import com.example.demo.bean.Role;
import com.example.demo.bean.User;
import com.example.demo.mapper.RolesMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 将需要用到自身id的都直接封装在这里，修改其他用户的不用
@Service
@Transactional
public class UserService implements UserDetailsService {
    // mapper装箱
    @Autowired
    UserMapper userMapper;
    @Autowired
    RolesMapper rolesMapper;
    // Spring Boot Security自带的密码加密
    // 这边可以根据继承自动搞吗
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        // 分两步操作，一个是得到用户，一个是获取用户权限，然后把权限给用户
        User user = userMapper.loadUserByName(s);
        System.out.println("查找到的用户：" + user.toString());
        // 检查是不是空的,空的差不到权限，抛出异常
        if (user == null){
            System.out.println("找不到该用户");
            return new User();
            // throw new UsernameNotFoundException("数据库中无此用户！");
        }
        // 不为空就查找并添加权限
        List<Role> rolesByUid = rolesMapper.getRolesByUid(user.getId());
        user.setRoles(rolesByUid);
        return user;
    }

    // 添加用户
    // -1代表重复
    // 0代表失败
    // 1代表成功
    public int InsertUser(User user) {
        // 先查找看有无重复
        User repeate = userMapper.loadUserByName(user.getUsername());
        if (repeate != null) {
            return -1;
        }
        // 插入前对密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 设置对象为可用
        user.setEnabled(true);
        // user表的处理结束，插入成功就是1
        long insertUserResu = userMapper.InsertAndReg(user);
        // 之后处理权限表，默认是普通用户，为2
        Long[] roles = new Long[]{(long) 2};
        // 返回的值是插入成功的条数，成功应为roles的大小
        int insertRoleResu = rolesMapper.addRoles(roles, user.getId());
        // 判断是否两个都成功
        boolean success = (insertRoleResu == roles.length) && insertUserResu == 1;
        if (success) {
            return 1;
        } else {
            return 0;
        }
    }
    // 剩下的就是无脑调用了
    public List<User> getUserByNickName(String nickname){
        return userMapper.getUserByNickName(nickname);
    }

    public int updateUserEmail(String email){
        return userMapper.updateUserEmail(email, Utils.getCurrentUser().getId());
    }

    public List<Role> getAllRole(){
        return userMapper.getAllRole();
    }

    public int updateUserEnabled(Boolean enabled, Long id){
        return userMapper.updateUserEnabled(enabled, id);
    }

    public int deleteUserById(Long id){
        return userMapper.deleteUserById(id);
    }

    public int deleteUserRolesByUid(Long uid){
        return userMapper.deleteUserRolesByUid(uid);
    }

    public int setUserRoles(Long[] rids, Long uid){
        return userMapper.setUserRoles(rids, uid);
    }

    public User getUserById(Long id){
        return userMapper.getUserById(id);
    }

    public int updateUserRoles(Long[] rids, Long uid){
        // 先删除所有,但是返回的结果不能作为判断，可能没有角色，也有可能有很多角色
        int i = userMapper.deleteUserRolesByUid(uid);
        // 再重新设置
        return userMapper.setUserRoles(rids, uid);
    }
}
