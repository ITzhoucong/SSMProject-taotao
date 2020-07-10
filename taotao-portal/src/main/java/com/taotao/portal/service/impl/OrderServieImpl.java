package com.taotao.portal.service.impl;

import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.portal.pojo.Order;
import com.taotao.portal.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author: ZhouCong
 * @date: Create in 2019/9/2 14:22
 * @description: 订单管理service
 */
@Service
public class OrderServieImpl implements OrderService{

    @Value("${ORDER_BASE_URL}")
    private String ORDER_BASE_URL;
    @Value("${ORDER_CREATE_URL}")
    private String ORDER_CREATE_URL;
    @Override
    public String createOrder(Order order) {
//          调用创建订单服务之前补全用户信息
//        从cookie中取TT_TOKEN的内容，根据token调用sso系统根据token换取用户信息

//        调用taotao-order服务提交订单
        String json = HttpClientUtil.doPostJson(ORDER_BASE_URL + ORDER_CREATE_URL, JsonUtils.objectToJson(order));
//        把json转换成TaotaoResult
        TaotaoResult taotaoResult = TaotaoResult.format(json);
        if (taotaoResult.getStatus() == 200){
            Object orderId =  taotaoResult.getData();
            return orderId.toString();
        }

        return "";
    }
}
