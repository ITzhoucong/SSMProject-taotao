package com.taotao.rest.service.impl;

import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CartItem;
import com.taotao.rest.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/29 20:55
 * @description: 订单列表绑定用户账户service
 */
@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_CART_KEY}")
    private String REDIS_CART_KEY;
    @Override
    public TaotaoResult saveCartItemList(String cartItemList,String username) {

        try {
        jedisClient.hset(REDIS_CART_KEY,username,cartItemList);

        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult getCartItemList(String username) {

        try {
        String json = jedisClient.hget(REDIS_CART_KEY, username);
//        把json转换成list
        List<CartItem> list = JsonUtils.jsonToList(json, CartItem.class);
        return TaotaoResult.ok(list);

        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,ExceptionUtil.getStackTrace(e));
        }
    }
}
