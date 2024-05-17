package com.moderatePerson.interceptor;

import com.moderatePerson.utils.jwt.JwtUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取Token
        String token = request.getHeader("Authorization");
        // 进行Token验证
        if (token != null && JwtUtil.verifyToken(token)!=null) {
            // 验证通过，放行请求
            return true;
        } else {
            // 验证失败，返回401 Unauthorized错误
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未授权的请求");
            return false;
        }
    }
}

