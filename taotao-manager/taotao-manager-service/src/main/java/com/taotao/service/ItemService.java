package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItem;

/**
 * @auther: ZhouCong
 * @date: 2019/7/27
 * @description:
 */
public interface ItemService {
     TbItem getItemById(Long itemId);

     EUDataGridResult getItemList(int page,int rows);

     TaotaoResult createItem(TbItem item,String desc,String itemParam) throws Exception;
}
