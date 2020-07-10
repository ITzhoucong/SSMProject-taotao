package com.taotao.search.dao.impl;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.Item;
import com.taotao.search.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/23 14:13
 * @description: 商品搜索Dao
 */
@Repository
public class SearchDaoImpl implements SearchDao{

    @Autowired
    private SolrServer solrServer;
    @Override
    public SearchResult search(SolrQuery query) throws Exception {

//        返回值对象
        SearchResult searchResult = new SearchResult();
//        根据查询条件查询索引库
        QueryResponse queryResponse = solrServer.query(query);
//        取查询结果
        SolrDocumentList solrDocumentList = queryResponse.getResults();
//        取查询结果总数量
        long numFound = solrDocumentList.getNumFound();
        searchResult.setRecordCount(numFound);
//        商品列表
        List<Item> Itemlist = new ArrayList<>();
//        取高亮显示
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

//        取商品列表
        for (SolrDocument entries : solrDocumentList) {
//            创建商品对象
            Item item = new Item();
            item.setId((String) entries.get("id"));
            List<String> list = highlighting.get(entries.get("id")).get("item_title");
            String title = "";
            if (list != null && list.size()>0){
               title = list.get(0);
            }else {
                title = (String) entries.get("item_title");
            }
            item.setTitle(title);
            item.setImage((String) entries.get("item_image"));
            item.setPrice((long) entries.get("item_price"));
            item.setSellPoint((String) entries.get("item_sell_point"));
            item.setCategoryName((String) entries.get("item_category_name"));
//            添加到商品列表
            Itemlist.add(item);

        }
        searchResult.setItemList(Itemlist);
        return searchResult;
    }
}
