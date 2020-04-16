package edu.whu.bookshop.conf;

import edu.whu.mSpring.annotation.Interceptor;
import edu.whu.mSpring.interceptor.HandlerInterceptor;
import edu.whu.mSpring.servlet.SessionHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Interceptor
public class LoginInterceptor implements HandlerInterceptor {

    private static String[] matchs = {"/user/login","/book/all","book/info","/user/register","/file/get"};

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String requestURI = req.getRequestURI();
        for(String s : matchs){
            String s2 = s.replace("*","");
            if(requestURI.startsWith(s2)){
                return true;
            }
        }

        if(req.getRequestedSessionId() != null &&SessionHelper.getSession(req.getRequestedSessionId()) != null){
            return true;
        } else {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse resp) throws Exception {

    }
}
