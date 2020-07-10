package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/19 16:50
 * @description: 内容管理
 */
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper contentMapper;
    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;
    @Value("${REST_CONTENT_SYNC_URL}")
    private String REST_CONTENT_SYNC_URL;

    @Override
    public EUDataGridResult getContentList(int page, int rows, long categoryId) {
//        创建pojo
        TbContentExample example = new TbContentExample();
//        设置查询条件
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
//       分页
        PageHelper.startPage(page, rows);
        List<TbContent> contentList = contentMapper.selectByExample(example);
//        创建返回对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(contentList);
        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(contentList);
        result.setTotal(pageInfo.getTotal());

        return result;
    }

    @Override
    public TaotaoResult insertContent(TbContent content) {
//        补全pojo
        content.setCreated(new Date());
        content.setUpdated(new Date());
        contentMapper.insert(content);

//        添加缓存同步
        try {
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult deleteContent(long[] ids) {

        TbContent content = contentMapper.selectByPrimaryKey(ids[0]);
        for (long id : ids) {
            contentMapper.deleteByPrimaryKey(id);
        }

//        添加缓存同步
        try {
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());

        } catch (Exception e) {
            e.printStackTrace();
        }


        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult updateContent(TbContent content) {
        contentMapper.updateByPrimaryKeySelective(content);

        //        添加缓存同步
        try {
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }
}
