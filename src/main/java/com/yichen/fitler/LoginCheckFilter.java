package com.yichen.fitler;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/admin/*") // 拦截所有访问 /admin 路径的请求
public class LoginCheckFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 获取 Session（不创建新 Session）
        HttpSession session = request.getSession(false);

        // 检查是否登录
        if (session == null || session.getAttribute("user") == null) {
            // 未登录：重定向到登录页
            response.sendRedirect(request.getContextPath() + "/login.html");
        } else {
            // 已登录：放行请求
            chain.doFilter(req, resp);
        }
    }

    @Override
    public void destroy() {

    }
}
