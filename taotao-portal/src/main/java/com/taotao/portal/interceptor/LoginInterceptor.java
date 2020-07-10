package com.taotao.portal.interceptor;

import com.taotao.common.utils.CookieUtils;
import com.taotao.pojo.TbUser;
import com.taotao.portal.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/29 10:14
 * @description:
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        在handler执行之前处理，返回值决定handler是否执行，true执行，false不执行
//        判断用户是否登录
//        从cookie中取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "TT_TOKEN");
//        根据token换取用户信息，调用sso系统的接口
        TbUser user = userService.getUserByToken(token);
//        跳转到登录页面，把用户请求的url作为参数传递给登录页面，返回false
        if (user == null) {
            httpServletResponse.sendRedirect(userService.SSO_BASE_URL + userService.SSO_PAGE_LOGIN + "?redirect=" + httpServletRequest.getRequestURL());
            return false;
        }
//        把用户信息放入request
        httpServletRequest.setAttribute("user",user);
//        取到用户信息，放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
//        在handler执行之后，返回modelandview之前处理
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
//        在返回modelandview之后处理,响应用户之后
    }
}
