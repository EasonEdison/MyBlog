package com.example.demo.mapper;

import com.example.demo.bean.Role;
import com.example.demo.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    // 通过name获取
    User loadUserByName(@Param("username") String username);
    // 插入 自动生成id并返回
    long InsertAndReg(User user);
    // 更新邮箱
    int updateUserEmail(@Param("email") String email, @Param("id") Long id);
    // 通过昵称查找，可能多个
    List<User> getUserByNickName(@Param("nickname") String nickname);

    List<Role> getAllRole();

    int updateUserEnabled(@Param("enabled") Boolean enabled, @Param("id") Long id);

    int deleteUserById(Long id);
    // 有个表专门管理角色和用户的对应
    int deleteUserRolesByUid(Long uid);

    int setUserRoles(@Param("rids") Long[] rids, @Param("uid") Long uid);

    User getUserById(Long id);
}
