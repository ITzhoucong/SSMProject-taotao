package com.zc.controller;

import com.zc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/21 12:27
 * @description:
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    @RequestMapping("/login1")
    public String login1(){
        return "login1";
    }
    @RequestMapping("/main")
    public String main(String username,String password){
        String userByName = userService.findUserByName(username);

        if (userByName == password){
            return "main";
        }

            return "login1";
    }
}
