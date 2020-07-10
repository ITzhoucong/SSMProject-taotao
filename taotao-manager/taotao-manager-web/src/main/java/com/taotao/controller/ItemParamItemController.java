package com.taotao.controller;

import com.taotao.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 展示商品规格参数
 * @author: ZhouCong
 * @date: Create in 2019/8/16 10:40
 * @description:
 */
@Controller
public class ItemParamItemController {

    @Autowired
    private ItemParamItemService itemParamItemService;

    @RequestMapping("/items/param/item/{itemId}")
    public String showItemParam(@PathVariable Long itemId, Model model) {
        String itemParamItem = itemParamItemService.geParamItemByItemId(itemId);
        model.addAttribute("itemParam", itemParamItem);
        return "item";
    }
}

