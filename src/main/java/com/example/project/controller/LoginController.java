package com.example.project.controller;

import com.example.project.Service.MiaoshaUserService;
import com.example.project.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/210:22
 */
@Controller
@Slf4j
@RequestMapping("/login")

public class LoginController {
    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/do_login")
    public String doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info("登录信息{}", loginVo.toString());
        miaoshaUserService.login(response, loginVo);
        return null;
    }
}
