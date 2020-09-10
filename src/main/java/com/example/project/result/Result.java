package com.example.project.result;

import lombok.Data;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/711:36
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public Result() {
        this.code = code;
        this.msg = msg;
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMessage();
        }
    }

    //成功的调用
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    //失败的调用
    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<T>(codeMsg);
    }

}
