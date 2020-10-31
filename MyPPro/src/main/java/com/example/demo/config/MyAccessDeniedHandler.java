package com.example.demo.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        // 响应状态为403禁止访问
        httpServletResponse.setStatus(403);
        // 设置响应格式
        httpServletResponse.setContentType("application/json;UTF-8");
        // 附带其他内容
        PrintWriter out = httpServletResponse.getWriter();
        out.write("权限不足！");
        out.flush();
        out.close();
    }
}
