package com.example.project.result;

import lombok.Data;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/210:50
 */
@Data
public class CodeMsg {
    private int code;
    private String message;

    public CodeMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500102, "秒杀失败");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500102, "秒杀重复");
    public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500103, "秒杀失败");
    //登录模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500216, "订单不存在");

}
