package com.example.project.controller;

import com.example.project.Service.GoodsService;
import com.example.project.Service.MiaoshaService;
import com.example.project.Service.MiaoshaUserService;
import com.example.project.Service.OrderService;
import com.example.project.config.GoodsKey;
import com.example.project.domain.MiaoshaOrder;
import com.example.project.domain.MiaoshaUser;
import com.example.project.domain.OrderInfo;
import com.example.project.rabbitmq.MQSender;
import com.example.project.rabbitmq.MiaoshaMessage;
import com.example.project.result.CodeMsg;
import com.example.project.result.Result;
import com.example.project.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/716:57
 */
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MQSender sender;
    @Autowired
    private MiaoshaService miaoshaService;
    private HashMap<Long, Boolean> localOverMap = new HashMap<>();


    /**
     * 系统初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {


        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if (goodsVoList == null) {
            return;
        }
        for (GoodsVo goods : goodsVoList) {

            redisTemplate.opsForValue().set(goods.getId(), goods.getStockCount(), GoodsKey.GetMiaoshaGoodsStock);
            localOverMap.put(goods.getId(), false);
        }

    }

    @RequestMapping(value = "/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> miaosha(Model model, @RequestParam("userId") String userId, @RequestParam("goodsId") long goodsId) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        MiaoshaUser miaoshaUser = miaoshaUserService.getById(Long.valueOf(userId));
        model.addAttribute("user", miaoshaUser);
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }


        //预减库存
        long stock = (long) redisTemplate.opsForValue().decrement(String.valueOf(goodsId));
        System.out.println(redisTemplate.opsForValue().get(String.valueOf(goodsId)));
        if (stock < 0) {
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(miaoshaUser.getId()), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(miaoshaUser);
        mm.setGoodsId(goodsId);
        sender.sendMiaoshaMessage(mm);
        System.out.println("排队中=====" + mm);

        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stocks = goods.getStockCount();
        if (stocks <= 0) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //判断是否秒杀到
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdGoodsId(Long.valueOf(miaoshaUser.getId()), goodsId);
        if (miaoshaOrder != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goods);

        return Result.success(orderInfo);

    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<MiaoshaOrder> miaoshaResult(Model model, @RequestParam("userId") String userId, @RequestParam("goodsId") long goodsId) {
        MiaoshaUser miaoshaUser = miaoshaUserService.getById(Long.parseLong(userId));
        model.addAttribute("user", miaoshaUser);
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        MiaoshaOrder result = miaoshaService.getMiaoshaResult(Long.parseLong(userId), goodsId);
        return Result.success(result);
    }

    @RequestMapping(value = "/verifyCode", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, @RequestParam("userId") String userId, @RequestParam("goodsId") long goodsId) {
        MiaoshaUser miaoshaUser = miaoshaUserService.getById(Long.valueOf(userId));
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image = miaoshaService.createVerifyCode(miaoshaUser, goodsId);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }


    }
}
