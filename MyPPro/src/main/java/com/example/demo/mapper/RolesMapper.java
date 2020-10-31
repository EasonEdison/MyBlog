package com.example.demo.mapper;

import com.example.demo.bean.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.List;

// 很简单，就根据用户id获取全部角色
@Mapper
public interface RolesMapper {

    int addRoles(@Param("roles") Long[] roles, @Param("uid") Long uid);

    List<Role> getRolesByUid(@Param("uid") Long uid);
}
