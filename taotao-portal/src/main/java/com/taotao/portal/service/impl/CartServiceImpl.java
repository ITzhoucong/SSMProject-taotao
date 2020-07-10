package com.taotao.portal.service.impl;

import com.taotao.common.utils.*;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.portal.pojo.CartItem;
import com.taotao.portal.service.CartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/29 12:25
 * @description: 购物车service
 */
@Service
public class CartServiceImpl implements CartService {

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${ITEM_INFO_URL}")
    private String ITEM_INFO_URL;
    @Value("${REST_CART_SAVE_URL}")
    private String REST_CART_SAVE_URL;
    @Value("${REST_CART_GETLIST_URL}")
    private String REST_CART_GETLIST_URL;

    @Autowired
    private UserServiceImpl userService;


    /**
     * @description: 添加购物车商品
     */
    @Override
    public TaotaoResult addCartItem(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
//        取商品信息
        CartItem cartItem = null;
//        取购物车商品列表
        List<CartItem> list = getCartItemList(request, response);
//        判断商品列表中是否存在此商品
        for (CartItem cItem : list) {
//            如果存在此商品
            if (cItem.getId() == itemId) {
//                商品数量添加
                cItem.setNum(cItem.getNum() + num);
                cartItem = cItem;
                break;
            }
        }
        if (cartItem == null) {
            cartItem = new CartItem();
//        根据商品id查询商品信息
            String josn = HttpClientUtil.doGet(REST_BASE_URL + ITEM_INFO_URL + itemId);
//        把json转换成Java对象
            TaotaoResult result = TaotaoResult.formatToPojo(josn, TbItem.class);
            if (result.getStatus() == 200) {
                TbItem item = (TbItem) result.getData();
                cartItem.setId(item.getId());
                cartItem.setTitle(item.getTitle());
                cartItem.setImage(item.getImage() == null ? "" : item.getImage().split(",")[0]);
                cartItem.setNum(num);
                cartItem.setPrice(item.getPrice());
            }
//            添加到购物车列表
            list.add(cartItem);
        }

        //        判断是否登录
        String username = getUsername(request);
//        如果用户登录
        if (username != null) {
            try {
//        把购物车列表写入redis
                Map<String, String> param = new HashMap<>();
                param.put("cartItemList", JsonUtils.objectToJson(list));
                HttpClientUtil.doPost(REST_BASE_URL + REST_CART_SAVE_URL + username, param);
                return TaotaoResult.ok();
            } catch (Exception e) {
                e.printStackTrace();
                return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
            }
        } else {
            //        把购物车列表写入cookie
            CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
        }


        return TaotaoResult.ok();
    }

    @Override
    public List<CartItem> getCartList(HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> list = getCartItemList(request, response);

        return list;
    }

    @Override
    public TaotaoResult addCartItemByNum(long itemId, int num, HttpServletRequest request, HttpServletResponse response) {
//        获取商品列表
        List<CartItem> list = getCartItemList(request, response);
        for (CartItem cItem : list) {
            if (cItem.getId() == itemId) {
//                设置数量为写入的数量
                cItem.setNum(num);

                break;
            }
        }
        //        判断是否登录
        String username = getUsername(request);
//        如果用户登录
        if (username != null) {
            try {
//        把购物车列表写入redis
                Map<String, String> param = new HashMap<>();
                param.put("cartItemList", JsonUtils.objectToJson(list));
                HttpClientUtil.doPost(REST_BASE_URL + REST_CART_SAVE_URL + username, param);
                return TaotaoResult.ok();
            } catch (Exception e) {
                e.printStackTrace();
                return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
            }
        } else {
            //        把购物车列表写入cookie
            CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(list), true);
        }


        return TaotaoResult.ok();

    }

    /**
     * @description: 删除列表商品
     */
    @Override
    public TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response) {
//        从cookie中把商品列表取出
        List<CartItem> cartItemList = getCartItemList(request, response);
//        从列表中找到商品
        for (CartItem cartItem : cartItemList) {
            if (cartItem.getId() == itemId) {
                cartItemList.remove(cartItem);
                break;
            }

        }
        //        判断是否登录
        String username = getUsername(request);
//        如果用户登录
        if (username != null) {
            try {
//        把购物车列表写入redis
                Map<String, String> param = new HashMap<>();
                param.put("cartItemList", JsonUtils.objectToJson(cartItemList));
                HttpClientUtil.doPost(REST_BASE_URL + REST_CART_SAVE_URL + username, param);
                return TaotaoResult.ok();
            } catch (Exception e) {
                e.printStackTrace();
                return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
            }
        } else {
            //        把购物车列表写入cookie
            CookieUtils.setCookie(request, response, "TT_CART", JsonUtils.objectToJson(cartItemList), true);
        }


        return TaotaoResult.ok();
    }

    /**
     * @description: 从redis和cookie中取商品列表
     */
    private List<CartItem> getCartItemList(HttpServletRequest request, HttpServletResponse response) {
        List<CartItem> listFormCookie = null;
        List<CartItem> listFormRedis = null;


//        从Cookie中取商品列表
        String tt_cart = CookieUtils.getCookieValue(request, "TT_CART", true);

        if (StringUtils.isNotBlank(tt_cart)) {

            //        把json转换成商品列表
            try {
                listFormCookie = JsonUtils.jsonToList(tt_cart, CartItem.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //        判断是否登录
        String username = getUsername(request);
//        如果用户登录
        if (username != null) {
//        从redis中取商品列表

            String cart = HttpClientUtil.doGet(REST_BASE_URL + REST_CART_GETLIST_URL + username);
            if (StringUtils.isNotBlank(cart)) {
                TaotaoResult result = TaotaoResult.formatToList(cart, CartItem.class);
                if (result.getStatus() == 200) {

                    listFormRedis = (List<CartItem>) result.getData();
                }
//                    判断cookie中是否有商品
                if (listFormCookie != null && listFormCookie.size() != 0) {
//                      判断redis是否为空
                    if (listFormRedis != null && listFormRedis.size() != 0) {
                        List<CartItem> list = new ArrayList<>();
                        CartItem cartItem = null;
                        //        判断商品列表中是否存在cookie中商品
                        for (CartItem cItem : listFormRedis) {
                            for (CartItem item : listFormCookie) {

//            如果存在此商品
                                if (cItem.getId() == item.getId()) {
//                商品数量添加
                                    Integer num = item.getNum();
                                    cItem.setNum(cItem.getNum() + num);
                                    list.add(cItem);
                                }else {
                                    list.add(item);
                                }
                            }
                        }
                        listFormRedis.addAll(list);
                    }
//                            清楚cookie
                        CookieUtils.deleteCookie(request, response, "TT_CART");
//        把cookie购物车列表写入redis
                    Map<String, String> param = new HashMap<>();
                    param.put("cartItemList", JsonUtils.objectToJson(listFormRedis));
                    HttpClientUtil.doPost(REST_BASE_URL + REST_CART_SAVE_URL + username, param);

                }

            }
            //        从redis中取同步cookie同步后的商品列表

            String cart2 = HttpClientUtil.doGet(REST_BASE_URL + REST_CART_GETLIST_URL + username);
            if (StringUtils.isNotBlank(cart2)) {
                TaotaoResult result = TaotaoResult.formatToList(cart, CartItem.class);
                if (result.getStatus() == 200) {

                    listFormRedis = (List<CartItem>) result.getData();
                }
            }

            if (listFormRedis != null && listFormRedis.size() != 0) {
                return listFormRedis;
            }
            if (listFormCookie != null && listFormCookie.size() != 0) {
                return listFormCookie;
            } else {
                return new ArrayList<>();
            }
        }

        if (listFormCookie != null && listFormCookie.size() != 0) {
            return listFormCookie;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * @description: 获取用户姓名
     */
    private String getUsername(HttpServletRequest request) {

        String tt_token = CookieUtils.getCookieValue(request, "TT_TOKEN");

        if (tt_token != null) {
            TbUser user = userService.getUserByToken(tt_token);

            if (user != null) {
                String username = user.getUsername();
                return username;
            }
        }
        return null;
    }
}
