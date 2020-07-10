package com.taotao.rest.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.rest.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/29 21:01
 * @description: 订单列表绑定用户账户controller
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping(value = "/save/{username}",method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult saveCartItemList(String cartItemList, @PathVariable("username") String username) {

        TaotaoResult result = cartService.saveCartItemList(cartItemList, username);
        return result;
    }

    @RequestMapping(value = "/getList/{username}")
    @ResponseBody
    public TaotaoResult getCartItemList(@PathVariable("username") String username){
        TaotaoResult result = cartService.getCartItemList(username);
        return result;
    }
}
