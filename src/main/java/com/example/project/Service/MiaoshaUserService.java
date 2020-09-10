package com.example.project.Service;

import com.example.project.domain.MiaoshaUser;
import com.example.project.expection.GlobalException;
import com.example.project.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/29:44
 */
public interface MiaoshaUserService {
    MiaoshaUser getById(long id);

    MiaoshaUser getByToken(HttpServletResponse response, String token);

    GlobalException login(HttpServletResponse response, LoginVo loginVo);

    void addCookie(HttpServletResponse response, String token, MiaoshaUser user);
}
