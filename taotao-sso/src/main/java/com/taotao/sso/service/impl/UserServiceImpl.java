package com.taotao.sso.service.impl;

import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author: ZhouCong
 * @date: Create in 2019/8/27 12:38
 * @description: 用户管理Service
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private JedisClient jedisClient;
    @Value("${REDIS_USER_SESSION}")
    private String REDIS_USER_SESSION;
    @Value(("${SSO_SESSION_EXPIRE}"))
    private Integer SSO_SESSION_EXPIRE;

    @Override
    public TaotaoResult checkData(String content, Integer type) {
//        创建查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
//        数据校验 可选参数1、2、3分别代表username、phone、email
//        用户名校验
        if (1 == type) {
            criteria.andUsernameEqualTo(content);
//            电话校验
        } else if (2 == type) {
            criteria.andPhoneEqualTo(content);
//            邮箱校验
        } else if (3 == type) {
            criteria.andEmailEqualTo(content);
        }
//        执行查询
        List<TbUser> list = userMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }

    @Override
    public TaotaoResult createUser(TbUser user) {
        user.setUpdated(new Date());
        user.setCreated(new Date());
//        md5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        userMapper.insert(user);
        return TaotaoResult.ok();
    }

    /**
     * 功能描述: 用户登录
     *
     * @Param: [username, password]
     * @Return: com.taotao.common.utils.TaotaoResult
     * @Author: ZhouCong
     * @Date: 2019/8/28 9:44
     */
    @Override
    public TaotaoResult userLogin(String username, String password,
                                  HttpServletRequest request, HttpServletResponse response) {
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = userMapper.selectByExample(example);
//        如果没有此用户名
        if (list == null || list.size() == 0) {
            return TaotaoResult.build(400, "用户名或密码错误");
        }
        TbUser user = list.get(0);
//        比对密码
        if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
            return TaotaoResult.build(400, "用户名或密码错误");
        }
//        生成token
        String token = UUID.randomUUID().toString();
//        保存用户之前，把用户对象中的密码清空
        user.setPassword(null);
//        把用户信息写入redis
        jedisClient.set(REDIS_USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
//        设置session的过期时间
        jedisClient.expire(REDIS_USER_SESSION + ":" + token, SSO_SESSION_EXPIRE);

//        添加cookie,有效期关闭浏览器失效
        CookieUtils.setCookie(request, response, "TT_TOKEN", token);
//        返回token
        return TaotaoResult.ok(token);
    }

    /**
     * 功能描述: 通过token查询用户信息,判断是否登录
     *
     * @Param: [token]
     * @Return: com.taotao.common.utils.TaotaoResult
     * @Author: ZhouCong
     * @Date: 2019/8/28 11:30
     */
    @Override
    public TaotaoResult getUserByToken(String token) {
//        根据token从redis中查询用户信息
        String json = jedisClient.get(REDIS_USER_SESSION + ":" + token);
        if (StringUtils.isBlank(json)) {
            return TaotaoResult.build(400, "此session已经过期，请重新登录");
        }
//        更新过期时间
        jedisClient.expire(REDIS_USER_SESSION + ":" + token, SSO_SESSION_EXPIRE);
//        文档要求返回的数据为pojo，不能为json
        return TaotaoResult.ok(JsonUtils.jsonToPojo(json, TbUser.class));
    }

    @Override
    public TaotaoResult loginOut(String token, HttpServletRequest request, HttpServletResponse response) {

        //        根据token从redis中查询用户信息
        String json = jedisClient.get(REDIS_USER_SESSION + ":" + token);
        if (!StringUtils.isBlank(json)) {
            jedisClient.del(REDIS_USER_SESSION + ":" + token);
            CookieUtils.deleteCookie(request,response,"TT_TOKEN");
            return TaotaoResult.ok();
        }
        return TaotaoResult.build(400, "该用户已经退出");
    }
}
