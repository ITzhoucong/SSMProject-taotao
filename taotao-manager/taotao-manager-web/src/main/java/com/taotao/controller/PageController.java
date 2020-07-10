package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转Controller
 *
 * @auther: ZhouCong
 * @date: 2019/7/27
 * @description:
 */
@Controller
public class PageController {
    /*
    * 打开首页
    * */
    @RequestMapping("/")
    public String showIndex() {
        return "index";
    }

/**
 * @description: 展示其他页面
 *
 */
    @RequestMapping("/{page}")
//    通过 @PathVariable 可以将 URL 中占位符参数绑定到控制器处理方法的入参中：
// URL 中的 {xxx} 占位符可以通过@PathVariable("xxx") 绑定到操作方法的入参中
    public String showPage(@PathVariable String page) {
        return page;
    }
}
