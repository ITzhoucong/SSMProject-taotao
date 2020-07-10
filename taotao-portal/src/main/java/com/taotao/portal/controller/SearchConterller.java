package com.taotao.portal.controller;

import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/23 17:58
 * @description: 商品搜索controller
 */
@Controller
public class SearchConterller {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/search")
    private String search(@RequestParam("q") String queryString, @RequestParam(value = "page",defaultValue = "1") Integer page , Model model){

        if (StringUtils.isNotBlank(queryString)){
            try {
                queryString = new String(queryString.getBytes("iso8859-1"),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        SearchResult searchResult = searchService.search(queryString, page);
//        向页传递参数
        model.addAttribute("query",queryString);
        model.addAttribute("totalPages",searchResult.getPageCount());
        model.addAttribute("itemList",searchResult.getItemList());
        model.addAttribute("page",page);

        return "search";
    }

}
