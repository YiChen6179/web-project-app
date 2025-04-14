package com.yichen.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 硬编码验证（实际项目需查数据库）
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if ("admin".equals(username) && "123".equals(password)) {
            // 登录成功：将用户名存入 Session
            HttpSession session = request.getSession();
            session.setAttribute("user", username);
            // 重定向到管理页
            response.sendRedirect("admin/admin.html");
        } else {
            // 登录失败：返回登录页
            response.sendRedirect("login.html");
        }
    }
}
