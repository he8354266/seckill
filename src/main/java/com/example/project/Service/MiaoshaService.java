package com.example.project.Service;

import com.example.project.domain.MiaoshaOrder;
import com.example.project.domain.MiaoshaUser;
import com.example.project.domain.OrderInfo;
import com.example.project.vo.GoodsVo;

import java.awt.image.BufferedImage;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/717:33
 */
public interface MiaoshaService {
    OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods);

    MiaoshaOrder getMiaoshaResult(Long userId, Long goodsId);

    BufferedImage createVerifyCode(MiaoshaUser miaoshaUser, long goodsId);


}
