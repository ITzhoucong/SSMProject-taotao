package com.taotao.rest.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatRestult;
import com.taotao.rest.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/17 16:09
 * @description: 商品分类服务
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${INDEX_ITEM_CAT_KEY}")
    private String INDEX_ITEM_CAT_KEY;

    @Override
    public CatRestult getItemCatList() {
        CatRestult catRestult = new CatRestult();
//        查询分类列表
        catRestult.setData(getCatList(0));

        return catRestult;
    }

    /**
     * @description: 查询分类列表
     */
    private List<?> getCatList(long parentId) {
        List<TbItemCat> list = null;
//        从缓存中取内容
        try {
            String result = jedisClient.hget(INDEX_ITEM_CAT_KEY, parentId + "");
            if (!StringUtils.isBlank(result)){
                 list = JsonUtils.jsonToList(result, TbItemCat.class);
            }else {
//                从Mysql中取数据
                TbItemCatExample example = new TbItemCatExample();
                TbItemCatExample.Criteria criteria = example.createCriteria();
//        执行查询
                criteria.andParentIdEqualTo(parentId);
                list = itemCatMapper.selectByExample(example);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        把内容添加到缓存
        try {
            String json = JsonUtils.objectToJson(list);
            jedisClient.hset("INDEX_ITEM_CAT_KEY",parentId+"",json);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        返回值list
                List resultList = new ArrayList<>();

//        向list中添加节点
        int count = 0;
        for (TbItemCat tbItemCat : list) {
//            判断是否为父节点
            if (tbItemCat.getIsParent()) {
                CatNode catNode = new CatNode();
                if (parentId == 0) {
                    catNode.setName("<a href='/products/" + tbItemCat.getId() + ".html'>"+tbItemCat.getName()+"</a>");
                } else {
                    catNode.setName(tbItemCat.getName());
                }
                catNode.setUrl("/products/" + tbItemCat.getId() + ".html");
                catNode.setItem(getCatList(tbItemCat.getId()));
                resultList.add(catNode);
                count ++;
//               第一层只取14条记录
                if (parentId == 0 && count >= 14){
                    break;
                }
//                如果是叶子节点
            }else {
                resultList.add("/products/" + tbItemCat.getId() + ".html|"+tbItemCat.getName());
            }
        }

        return resultList;
    }
}
