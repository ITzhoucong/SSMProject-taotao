package com.taotao.search.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/23 12:27
 * @description: 索引库维护
 */
@Controller
@RequestMapping("/manager")
public class ItemController {

    @Autowired
    private ItemService itemService;

    /**
     * 功能描述:导入商品数据到索引库
     *
     * @Param:
     * @Return:
     * @Author: ZhouCong
     * @Date: 2019/8/23 12:28
     */
    @RequestMapping("/importall")
    @ResponseBody
    public TaotaoResult importAllItems() {
        TaotaoResult result = itemService.importAllItems();
        return result;
    }
}
