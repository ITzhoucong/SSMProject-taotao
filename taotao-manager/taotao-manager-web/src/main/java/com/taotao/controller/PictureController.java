package com.taotao.controller;

import com.taotao.common.utils.JsonUtils;
import com.taotao.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @auther: ZhouCong
 * @date: Create in 2019/8/4 13:18
 * @description: 上传图片处理
 */
@Controller
public class PictureController {
    @Autowired
    private PictureService pictureService;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String pictureUpload(MultipartFile uploadFile){
        Map resulst = pictureService.uploadPicture(uploadFile);
//        谷歌浏览器可以使用，为了保证功能兼容性,在其他浏览器也可以使用，需要把Result转换成json格式的字符串
        String json = JsonUtils.objectToJson(resulst);
        return json;
    }


}
