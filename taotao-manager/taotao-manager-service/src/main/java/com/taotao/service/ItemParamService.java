package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbItemParam;
import org.springframework.stereotype.Service;

@Service
public interface ItemParamService {

    EUDataGridResult getItemParamList(int page,int rows);
    TaotaoResult getItemParamByCid(long cid);
    TaotaoResult insertItemParam(TbItemParam itemParam);
}
