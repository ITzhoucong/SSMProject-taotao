package com.taotao.rest.controller;

import com.taotao.rest.pojo.CatRestult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/17 17:51
 * @description: 商品分类列表
 */
@Controller
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;
/**
 * 功能描述: 第一种方式实现
 *
 * @Param:
 * @Return:
 * @Author: ZhouCong
 * @Date: 2019/8/17 18:09
 */
  /*  @RequestMapping(value = "/itemcat/list",produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String getItemCatList(String callback) {
        CatRestult catRestult = itemCatService.getItemCatList();
//        把pojo转换成字符串
        String json = JsonUtils.objectToJson(catRestult);
//        拼装返回值
        String result = callback + "("+json+");";
        return result;
    }*/

/**
 * 功能描述: 第二种方式实现，springmvc 4.1及以上版本才能使用
 *
 * @Param:
 * @Return:
 * @Author: ZhouCong
 * @Date: 2019/8/17 18:08
 */
    @RequestMapping("/itemcat/list")
    @ResponseBody
    public Object getItemCatList(String callback){
        CatRestult catRestult = itemCatService.getItemCatList();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(catRestult);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }
}
