package com.taotao.portal.pojo;

import com.taotao.pojo.TbItem;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/26 13:25
 * @description: 商品详情展示pojo扩展
 */
public class ItemInfo extends TbItem {

    public String[] getImages() {

        String image = getImage();
        if (image != null) {
            String[] images = image.split(",");
            return images;
        }
        return null;
    }
}
