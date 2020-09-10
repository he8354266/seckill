package com.example.project.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/210:33
 */
@Data
public class LoginVo {

    private String mobile;

    private String password;

}
