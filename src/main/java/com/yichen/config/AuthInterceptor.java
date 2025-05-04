package com.yichen.config;

import com.alibaba.fastjson.JSON;
import com.yichen.utils.UserSession;
import com.yichen.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证拦截器，拦截未登录用户访问需要认证的接口
 */
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    
    private final UserSession userSession;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 放行登录、登出接口
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/auth/login") || requestURI.contains("/auth/logout")) {
            return true;
        }
        
        // 放行Knife4j的相关接口
        if (requestURI.contains("/doc.html") || 
            requestURI.contains("/swagger") || 
            requestURI.contains("/v2/api-docs") || 
            requestURI.contains("/webjars/")) {
            return true;
        }
        
        // 检查用户是否已登录
        if (!userSession.isAuthenticated()) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(Result.error(401, "用户未登录")));
            return false;
        }
        
        return true;
    }
} 