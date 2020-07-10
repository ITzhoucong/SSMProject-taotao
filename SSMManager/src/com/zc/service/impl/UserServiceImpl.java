package com.zc.service.impl;

import com.zc.mapper.UserMapper;
import com.zc.pojo.User;
import com.zc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/21 13:07
 * @description:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public String findUserByName(String username) {
        User user = userMapper.selectUserByUsername(username);
        String password = user.getPassword();
        return password;
    }
}
