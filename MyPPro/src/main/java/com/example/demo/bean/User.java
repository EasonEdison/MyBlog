package com.example.demo.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class User implements UserDetails {
    // 数据库的int 11 都是long
    private Long id;
    private String username;
    private String nickname;
    private String password;
    // tinyint(1), 只能存放0和1，对应的就是boolean类型
    private boolean enabled;
    private String email;
    // varchar(255)这么大
    private String userface;
    private Timestamp regTime;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", userface='" + userface + '\'' +
                ", regTime=" + regTime +
                ", roles=" + roles +
                '}';
    }

    // 额外加了roles, 因为这个类要做权限验证
    // 注意，role本身也是一个表，不能直接可User绑定到一起，不然User没了，roles也就随着消失了
    private List<Role> roles;

    public Long getId() {
        return id;
    }

    // 写成long了
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isEnabled() {
        // 账号是否被禁用，根据数据库的状态决定
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserface() {
        return userface;
    }

    public void setUserface(String userface) {
        this.userface = userface;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Timestamp getRegTime() {
        return regTime;
    }

    public void setRegTime(Timestamp regTime) {
        this.regTime = regTime;
    }


    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        // 设置账号永不过期
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        // 设置账号永不被锁定
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        // 设置凭证永不过期
        return true;
    }




    // 这个方法获得所有的权限，每个权限都会生成一个验证类，返回这个类的集合作为判断
    // List继承Collection，所以可以用List
    @Override
    @JsonIgnore
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorList = new ArrayList<>();
        for (Role role : roles){
            // 将roles变成实例类，再送到List中
            authorList.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authorList;
    }

}
