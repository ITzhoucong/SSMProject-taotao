package com.taotao.search.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.service.AddItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/24 16:40
 * @description:
 */
@Controller
@RequestMapping("/manager")
public class AddItemController {
    @Autowired
    private AddItemService addItemService;

    @RequestMapping(value = "/importone" ,method = RequestMethod.GET)
    public TaotaoResult addItem(@RequestParam("id") String itemId){

        TaotaoResult result = addItemService.addItem(itemId);
        return result;
    }
}
