package com.example.project.expection;

import com.example.project.result.CodeMsg;
import lombok.Data;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/210:54
 */
@Data
public class GlobalException extends RuntimeException {
    private CodeMsg codeMsg;
    public GlobalException(CodeMsg codeMsg){
        this.codeMsg =codeMsg;
    }

}
