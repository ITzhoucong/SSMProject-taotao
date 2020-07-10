package com.taotao.rest.service;

import com.taotao.common.utils.TaotaoResult;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/29 20:50
 * @description:
 */
public interface CartService {

    TaotaoResult saveCartItemList(String cartItemList,String username);

    TaotaoResult getCartItemList(String username);
}
