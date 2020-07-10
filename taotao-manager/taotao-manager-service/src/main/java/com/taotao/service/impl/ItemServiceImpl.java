package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品管理service
 *
 * @auther: ZhouCong
 * @date: 2019/7/27
 * @description:
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper itemDescMapper;
    @Autowired
    private TbItemParamItemMapper itemParamItemMapper;
    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Value("${SEARCH_MANAGER_BASE_URL}")
    private String SEARCH_MANAGER_BASE_URL;

    @Override
    public TbItem getItemById(Long itemId) {
//        TbItem item = itemMapper.selectByPrimaryKey(itemId);
//        添加查询条件
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            TbItem item = list.get(0);
            return item;
        }
        return null;
    }

    /**
     * @description: 商品列表查询
     */
    @Override
    public EUDataGridResult getItemList(int page, int rows) {
//        查询商品列表
        TbItemExample example = new TbItemExample();
//        分页处理
        PageHelper.startPage(page, rows);
        List<TbItem> list = itemMapper.selectByExample(example);
//        创建一个返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
//        取记录总条数
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    /**
     * @description: 新增商品
     */
    @Override
    public TaotaoResult createItem(TbItem item, String desc, String itemParam) throws Exception {
        // 获取事务定义
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 设置事务隔离级别，开启新事务
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = transactionManager.getTransaction(definition);

//        生成商品ID
        Long itemId = IDUtils.genItemId();
        try {
            //        item补全
            item.setId(itemId);
//        商品状态，1-正常，2-下架，3-删除
            item.setStatus((byte) 1);
            item.setCreated(new Date());
            item.setUpdated(new Date());
//        插入数据库
            int insert = itemMapper.insert(item);
//        添加商品描述信息
            TaotaoResult result = insertItemDesc(itemId, desc);
            if (result.getStatus() != 200) {
                throw new Exception();
            }
//        添加规格参数
            result = insertItemParamItem(itemId, itemParam);
            if (result.getStatus() != 200) {
                throw new Exception();
            }

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);

        }
    //      请求solr服务添加数据
        Map map = new HashMap();
        map.put("id", itemId + "");
        HttpClientUtil.doGet(SEARCH_MANAGER_BASE_URL + "/importone", map);

        return TaotaoResult.ok();
    }

    /**
     * @description: 添加商品描述
     */
    private TaotaoResult insertItemDesc(Long itemId, String desc) {
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        itemDescMapper.insert(tbItemDesc);
        return TaotaoResult.ok();
    }

    /**
     * @description: 添加规格参数
     */
    private TaotaoResult insertItemParamItem(Long itemId, String itemParam) {
//        创建一个pojo
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParam);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(new Date());
//        向表中插入数据
        itemParamItemMapper.insert(itemParamItem);
        return TaotaoResult.ok();
    }

}
