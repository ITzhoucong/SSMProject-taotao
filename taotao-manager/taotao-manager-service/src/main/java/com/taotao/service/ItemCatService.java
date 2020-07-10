package com.taotao.service;

import com.taotao.common.pojo.EUTreeNode;

import java.util.List;

/**
 * @auther: ZhouCong
 * @date: Create in 2019/8/2 15:32
 * @description:
 */
public interface ItemCatService {
    List<EUTreeNode> getCatList(long parentId);
}
