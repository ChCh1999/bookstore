package edu.whu.mSpring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerInterceptor {
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp) throws Exception;
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
