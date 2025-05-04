package com.yichen.config;

import com.alibaba.fastjson.JSON;
import com.yichen.entity.User;
import com.yichen.mapper.UserMapper;
import com.yichen.utils.JwtUtil;
import com.yichen.utils.UserContext;
import com.yichen.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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
    
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserContext userContext;
    
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
        
        // 获取Authorization请求头中的Token
        String authHeader = request.getHeader("token");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(Result.error(401, "未提供有效的认证信息")));
            return false;
        }
        
        // 提取Token并验证
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(JSON.toJSONString(Result.error(401, "认证信息已过期或无效")));
            return false;
        }
        
        // Token有效，获取用户信息并设置到上下文
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId != null) {
            User user = userMapper.selectById(userId);
            if (user != null) {
                userContext.setCurrentUser(user);
                return true;
            }
        }
        
        // 用户不存在
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(JSON.toJSONString(Result.error(401, "用户不存在或已被删除")));
        return false;
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后清理上下文
        userContext.clear();
    }
} 