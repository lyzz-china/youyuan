package com.youyuan.interceptor;

import com.youyuan.service.UserService;
import com.youyuan.vo.OnlineUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*
* @author yizhong.liao
* @createTime 2021/9/2 14:35
*/
@Slf4j
@Component
public class ApiAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    private static final String REQUEST_METHOD = "OPTIONS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("ApiAccessInterceptor preHandle process uri:{}, method = {}",request.getRequestURI(),request.getMethod());
        if (request.getMethod().equalsIgnoreCase(REQUEST_METHOD)) {
            return true;
        }
        OnlineUser onlineUser = userService.findOnlineUser(request);
        if (onlineUser == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
