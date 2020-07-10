package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbItemParamMapper;
import com.taotao.pojo.TbItemParam;
import com.taotao.pojo.TbItemParamExample;
import com.taotao.service.ItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/10 19:34
 * @description:
 */
@Service
public class ItemParamServiceImpl implements ItemParamService {

    @Autowired
    private TbItemParamMapper itemParamMapper;

    @Override
    public EUDataGridResult getItemParamList(int page, int rows) {
//        创建pojo
        TbItemParamExample example = new TbItemParamExample();
//        分页处理
        PageHelper.startPage(page, rows);
        List<TbItemParam> paramList = itemParamMapper.selectByExampleWithBLOBs(example);
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(paramList);
        PageInfo pageInfo = new PageInfo(paramList);
        result.setTotal(pageInfo.getTotal());

        return result;
    }

    @Override
    public TaotaoResult getItemParamByCid(long cid) {

        TbItemParamExample example = new TbItemParamExample();
        TbItemParamExample.Criteria criteria = example.createCriteria();
        criteria.andItemCatIdEqualTo(cid);
        List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
//        判断是否查询到结果
        if (list != null && list.size() > 0) {
            return TaotaoResult.ok(list.get(0));
        }
        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult insertItemParam(TbItemParam itemParam) {
        //补全pojo
        itemParam.setCreated(new Date());
        itemParam.setUpdated(new Date());
//        插入到规格参数模板表
        itemParamMapper.insert(itemParam);
        return TaotaoResult.ok();
    }
}
