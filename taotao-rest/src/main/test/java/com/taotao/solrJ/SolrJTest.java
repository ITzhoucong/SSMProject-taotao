package com.taotao.solrJ;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/23 10:23
 * @description:
 */
public class SolrJTest {

    @Test
    public void addDocument() throws Exception{
//        创建一个连接
        SolrServer solrServer = new HttpSolrServer("http://192.168.174.128:8080/solr");
//        创建文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id","test001");
        document.addField("item_title","测试商品1");
        document.addField("item_price",123456);
//        把文档对象写入索引库
        solrServer.add(document);
//        提交
        solrServer.commit();
    }
    @Test
    public void deleteDocument() throws Exception{
        //        创建一个连接
        SolrServer solrServer = new HttpSolrServer("http://192.168.174.128:8080/solr");
        //solrServer.deleteById("test001");
        solrServer.deleteByQuery("*:*");
        solrServer.commit();
    }

    @Test
    public void queryDocument() throws Exception{
        SolrServer solrServer = new HttpSolrServer("http://192.168.174.128:8080/solr");
//        创建查询对象
        SolrQuery query = new SolrQuery();
//        设置查询条件
        query.setQuery("*:*");
//        设置查询数量，默认从0取10条
        query.setStart(20);
        query.setRows(50);
//        执行查询
        QueryResponse response = solrServer.query(query);
//      取查询结果
        SolrDocumentList results = response.getResults();
        System.out.println("共查询到记录:"+ results.getNumFound());
        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("item_title"));
            System.out.println(result.get("item_price"));


        }
    }
}
