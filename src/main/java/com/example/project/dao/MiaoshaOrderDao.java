package com.example.project.dao;

import com.example.project.domain.MiaoshaOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/917:27
 */
@Mapper
public interface MiaoshaOrderDao {
    @Select("select * from miaosha_order where user_id=#{userId} and goods_id=#{goodsId}")
    public MiaoshaOrder getMiaoshaResult(@Param("userId")long userId,@Param("goodsId")long goodsId);
}
