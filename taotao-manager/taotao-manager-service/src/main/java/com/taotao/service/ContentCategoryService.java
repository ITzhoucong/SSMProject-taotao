package com.taotao.service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.utils.TaotaoResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContentCategoryService {

    /**
     * @description: 展示分类列表
     *
     */
    List<EUTreeNode> getCategoryList(long parentId);
    /**
     * @description: 插入分类
     *
     */
    TaotaoResult insertContentCategory(long parentId,String name);
    /**
     * @description: 删除分类
     *
     */
    TaotaoResult deleteContentCategory(long id);

    /**
     * @description: 重命名分类
     *
     */
    TaotaoResult updateContentCategory(long id,String name);

}
