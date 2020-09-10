package com.example.project.util;

import java.util.UUID;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/214:24
 */
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
