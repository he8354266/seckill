package com.example.project.Service.impl;

import com.example.project.Service.MiaoshaUserService;
import com.example.project.constant.RedisConstant;
import com.example.project.dao.MiaoshaUserDao;
import com.example.project.domain.MiaoshaUser;
import com.example.project.expection.GlobalException;
import com.example.project.result.CodeMsg;
import com.example.project.util.MD5Util;
import com.example.project.util.UUIDUtil;
import com.example.project.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/29:57
 */
@Service
public class MiaoshaUserServiceImpl implements MiaoshaUserService {
    public static final String COOKI_NAME_TOKEN = "token";
    @Autowired
    private MiaoshaUserDao miaoshaUserDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public MiaoshaUser getById(long id) {

        return miaoshaUserDao.getById(id);
    }

    @Override
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        return null;
    }

    @Override
    public GlobalException login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        if (miaoshaUser == null) {
            return new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //验证密码
        String dbPass = miaoshaUser.getPassword();
        String saltDB = miaoshaUser.getSalt();
        String calcPass = MD5Util.formPassToDBPass(password, saltDB);
        if (!password.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        String token = UUIDUtil.uuid();
        addCookie(response, token, miaoshaUser);
        return null;
    }

    @Override
    public void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), token, RedisConstant.EXPIRE);
        Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
        cookie.setMaxAge(RedisConstant.EXPIRE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
