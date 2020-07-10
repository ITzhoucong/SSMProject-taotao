package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;
import org.springframework.stereotype.Service;

@Service
public interface ContentService {

    public EUDataGridResult getContentList(int page,int rows,long categoryId);

    TaotaoResult insertContent(TbContent content);

    TaotaoResult deleteContent(long[] ids);

    TaotaoResult updateContent(TbContent content);
}
