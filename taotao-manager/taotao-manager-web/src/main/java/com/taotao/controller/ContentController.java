package com.taotao.controller;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/19 17:14
 * @description: 内容管理
 */
@Controller
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    @RequestMapping("/query/list")
    @ResponseBody
    public EUDataGridResult getContentList(Integer page,Integer rows,Long categoryId){
        EUDataGridResult result = contentService.getContentList(page, rows, categoryId);
        return result;
    }
    @RequestMapping("/save")
    @ResponseBody
    public TaotaoResult insertContent(TbContent content){
        TaotaoResult result = contentService.insertContent(content);
        return result;
    }
    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult deleteContent(@RequestParam("ids") long[] ids) {

            TaotaoResult result = contentService.deleteContent(ids);
            return result;

    }
    @RequestMapping(value = "/edit" ,method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult updateContent(TbContent content){
        TaotaoResult result = contentService.updateContent(content);
        return result;
    }
}
