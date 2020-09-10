package com.example.project.Service.impl;

import com.example.project.Service.GoodsService;
import com.example.project.Service.MiaoshaService;
import com.example.project.Service.OrderService;
import com.example.project.config.MiaoshaKey;
import com.example.project.dao.MiaoshaOrderDao;
import com.example.project.domain.MiaoshaOrder;
import com.example.project.domain.MiaoshaUser;
import com.example.project.domain.OrderInfo;
import com.example.project.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/717:35
 */
@Service

public class MiaoshaServiceImpl implements MiaoshaService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MiaoshaOrderDao miaoshaOrderDao;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public OrderInfo miaosha(MiaoshaUser miaoshaUser, GoodsVo goods) {
        //减库存下订单 写入秒杀订单
        goodsService.reduceStock(goods);
        return orderService.createOrder(miaoshaUser, goods);
    }

    @Override
    public MiaoshaOrder getMiaoshaResult(Long userId, Long goodsId) {
        return miaoshaOrderDao.getMiaoshaResult(userId, goodsId);
    }

    @Override
    public BufferedImage createVerifyCode(MiaoshaUser miaoshaUser, long goodsId) {
        if (miaoshaUser == null || goodsId <= 0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        //set color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        //draw border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        //生成随机数
        Random rdm = new Random();
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis
        int rnd = calc(verifyCode);
        redisTemplate.opsForValue().set(miaoshaUser.getId() + "," + goodsId,String.valueOf(rnd) , MiaoshaKey.GetMiaoshaVerifyCode);
        return image;
    }

    private static char[] ops = new char[]{'+', '-', '*'};

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (int) engine.eval(exp);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }

    }

    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = "" + num1 + op1 + num2 + op2 + num3;
        return exp;
    }
}
