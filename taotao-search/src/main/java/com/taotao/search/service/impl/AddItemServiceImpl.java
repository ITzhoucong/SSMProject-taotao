package com.taotao.search.service.impl;

import com.taotao.common.utils.ExceptionUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.search.mapper.SelectItemMapper;
import com.taotao.search.pojo.Item;
import com.taotao.search.service.AddItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/24 16:31
 * @description:
 */
@Service
public class AddItemServiceImpl implements AddItemService {

    @Autowired
    private SelectItemMapper selectItemMapper;
    @Autowired
    private SolrServer solrServer;

    @Override
    public TaotaoResult addItem(String itemId) {
        Item item = selectItemMapper.selectItemById(itemId);
        try {
            //            创建一个SolrInputDocument
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id", itemId);
            document.setField("item_title", item.getTitle());
            document.setField("item_sell_point", item.getSellPoint());
            document.setField("item_price", item.getPrice());
            document.setField("item_image", item.getImage());
            document.setField("item_category_name", item.getCategoryName());
            document.setField("item_desc", item.getItemDes());
//            写入索引库
            solrServer.add(document);


//        提交修改
            solrServer.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok();
    }
}
