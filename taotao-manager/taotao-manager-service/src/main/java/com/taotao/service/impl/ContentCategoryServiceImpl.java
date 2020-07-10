package com.taotao.service.impl;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/19 10:16
 * @description: 内容分类管理
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    @Override
    public List<EUTreeNode> getCategoryList(long parentId) {
//        根据parentId查询节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
//        执行查询
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        List<EUTreeNode> resultList = new ArrayList<>();
        for (TbContentCategory contentCategory : list) {
//            创建一个节点
            EUTreeNode node = new EUTreeNode();
            node.setId(contentCategory.getId());
            node.setText(contentCategory.getName());
            node.setState(contentCategory.getIsParent() ? "closed" : "open");

            resultList.add(node);
        }
        return resultList;
    }

    @Override
    public TaotaoResult insertContentCategory(long parentId, String name) {
//        创建pojo
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setName(name);
        contentCategory.setIsParent(false);
//        状态可选值：1(正常),2(删除)
        contentCategory.setStatus(1);
        contentCategory.setParentId(parentId);
        contentCategory.setSortOrder(1);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
//        添加记录
        contentCategoryMapper.insert(contentCategory);
//        查看父节点的isParent是否为true ，如果不是true改为true
        TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
//        判断是否为true
        if (!parentCat.getIsParent()) {
            parentCat.setIsParent(true);
//            更新父节点
            contentCategoryMapper.updateByPrimaryKey(parentCat);
        }
//        返回结果
        return TaotaoResult.ok(contentCategory);
    }

    @Override
    public TaotaoResult deleteContentCategory(long id) {
//        获取parentId
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        Long parentId = contentCategory.getParentId();
//        删除
        contentCategoryMapper.deleteByPrimaryKey(id);
//        判断parentid对应的记录下是否有子节点。如果没有子节点。需要把parentid对应的记录的isparent改成false
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria2 = example.createCriteria();
        criteria2.andParentIdEqualTo(parentId);
        int count = contentCategoryMapper.countByExample(example);
        TbContentCategory parentCat = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (count == 0) {
            if (parentCat.getIsParent()) {
                parentCat.setIsParent(false);
                //            更新父节点
                contentCategoryMapper.updateByPrimaryKey(parentCat);

            }
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContentCategory(long id, String name) {
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        contentCategory.setName(name);
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        return TaotaoResult.ok();
    }
}
