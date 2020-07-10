package com.zc.mapper;

import com.zc.pojo.User;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/21 13:00
 * @description:
 */
public interface UserMapper {

    User selectUserByUsername (String username);
}
