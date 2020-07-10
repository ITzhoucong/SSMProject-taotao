package com.taotao.portal.controller;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/29 13:19
 * @description: 购物车controller
 */
@Controller
@RequestMapping("/cart")
public class CartContoller {

    @Autowired
    private CartService cartService;

    @RequestMapping("/add/{itemId}")
    public String addCartItem(@PathVariable("itemId") long itemId, @RequestParam(defaultValue = "1") Integer num, HttpServletRequest request, HttpServletResponse response) {
        TaotaoResult result = cartService.addCartItem(itemId, num, request, response);

        return "redirect:/cart/success.html";
    }

    @RequestMapping("/success")
    public String showSuccess() {
        return "cartSuccess";
    }

    @RequestMapping("/cart")
    public String showCart(HttpServletRequest request, HttpServletResponse response, Model model) {
//        获取商品列表
        List<CartItem> cartItemList = cartService.getCartList(request, response);
//        获取商品数
        int cartSize = cartItemList.size();
        model.addAttribute("cartList", cartItemList);
        model.addAttribute("cartSize",cartSize);
        return "cart";
    }

    @RequestMapping("/update/num/{itemId}")
    @ResponseBody
    public TaotaoResult addCartItemByNum(HttpServletRequest request, HttpServletResponse response, Integer num, @PathVariable("itemId") Long itemId) {
        TaotaoResult result = cartService.addCartItemByNum(itemId, num, request, response);
        return result;
    }

    @RequestMapping("/delete/{itemId}")
    public String deleteCartItem(@PathVariable("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response) {
        cartService.deleteCartItem(itemId, request, response);
        return "redirect:/cart/cart.html";
    }
}
