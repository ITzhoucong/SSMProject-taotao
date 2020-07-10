package com.taotao.search.pojo;

import java.util.List;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/23 14:05
 * @description:
 */
public class SearchResult {

    /**
     * @description: 商品列表
     *
     */
    private List<Item> itemList;

    /**
     * @description: 总记录数
     *
     */
    private long recordCount;
   /**
    * @description: 总页数
    *
    */
   private long pageCount;

   /**
    * @description: 当前页
    *
    */
   private long curPage;

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getCurPage() {
        return curPage;
    }

    public void setCurPage(long curPage) {
        this.curPage = curPage;
    }
}
