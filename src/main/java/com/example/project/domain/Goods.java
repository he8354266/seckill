package com.example.project.domain;

import lombok.Data;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/79:13
 */
@Data
public class Goods  {
    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;
}
