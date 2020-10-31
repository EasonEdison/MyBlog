package com.example.demo.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class MyPasswordEncoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence password) {
        System.out.println("密码是：" + DigestUtils.md5DigestAsHex(password.toString().getBytes()));
        // 掉包加密
        return DigestUtils.md5DigestAsHex(password.toString().getBytes());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 匹配一下, raw 未加工的
        // return encodedPassword.equals(DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()));
        return encodedPassword.equals(this.encode(rawPassword));
    }
}
