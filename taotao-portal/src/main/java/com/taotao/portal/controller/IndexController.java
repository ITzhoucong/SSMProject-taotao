package com.taotao.portal.controller;

import com.taotao.portal.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/16 16:12
 * @description:
 */
@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("index")
    public String showIndex(Model model) {
        String adJson = contentService.getContentList();
        model.addAttribute("ad1", adJson);

        return "index";
    }
}
