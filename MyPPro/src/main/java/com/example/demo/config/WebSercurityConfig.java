package com.example.demo.config;

import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Configuration
public class WebSercurityConfig extends WebSecurityConfigurerAdapter {

    // 装箱服务类
    @Autowired
    UserService userService;
    // 装箱自定义的验证方法
    @Autowired
    MyAuthenProvider myAuthenProvider;
    // 装箱权限不足类
    @Autowired
    MyAccessDeniedHandler myAccessDeniedHandler;


    // 设置验证方法
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{

        // 还可以在内存中创建用户
        // 注意密码要加密
        auth.inMemoryAuthentication()
                .passwordEncoder(new MyPasswordEncoder())
                .withUser("Eason")
                // 注册了密码加密后，要用加密的密码
                .password(new MyPasswordEncoder().encode("123"))
                .roles("超级管理员");

        // 还可以注册自定义的验证类
        auth.authenticationProvider(myAuthenProvider);
        // 指定UserDetailsService的实现类
        auth.userDetailsService(userService).passwordEncoder(new MyPasswordEncoder());

    }


    // 处理前端请求。可以直接使用

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()    // 开启支持
                // 做URL匹配，后面的作用是要求登录(为什么要把这个插在前面？)
                .antMatchers("/admin/category/all").authenticated()
                // 匹配前后缀，指定唯一权限角色可访问
                .antMatchers("/admin/**", "/reg").hasRole("超级管理员")
                // 后面的是其他的任意URL，都可以访问，验证即可
                .anyRequest().authenticated()
                // formLogin是支持基于表单的验证，而后面的指定登录位置,自动跳转的位置？
                .and().formLogin().loginPage("/login_page") // 这个好像被Controller自己管理了？
                // 成功后的处理
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        // 在响应头里面传递成功信息
                        // 设置报文格式
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        // 设置报文内容
                        PrintWriter out = httpServletResponse.getWriter();
                        // 注意”里面的“要用转义字符
                        out.write("{\"status\":\"success\",\"msg\":\"登录成功\"}");
                        // 使用sync的方式刷新缓冲区中的流
                        out.flush();
                        out.close();
                    }
                })
                // 失败后的处理
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        out.write("{\"status\":\"error\",\"msg\":\"登陆失败\"}");
                        out.flush();
                        out.close();
                    }
                })
                // 指定登录验证的页面
                .loginProcessingUrl("/login")
                // 指定传过来的信息中用来验证的参数
                .usernameParameter("username").passwordParameter("password").permitAll()
                // 设置退出的信息, 允许所有的退出操作
                .and().logout().permitAll()
                // 对于CSRF保护的支持的设置, 这里是关掉了
                .and().csrf().disable()
                // 添加处理异常的方法, 这里设置了如果被拒绝了怎么办
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);

    }

    // 资源、网页过滤

    @Override
    public void configure(WebSecurity web) throws Exception {
        // 后面的是免登录就能访问的
        web.ignoring().antMatchers("/index.html", "/static/**");
    }
}
