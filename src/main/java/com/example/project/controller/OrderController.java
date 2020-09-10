package com.example.project.controller;

import com.example.project.Service.GoodsService;
import com.example.project.Service.MiaoshaUserService;
import com.example.project.Service.OrderService;
import com.example.project.domain.MiaoshaUser;
import com.example.project.domain.OrderInfo;
import com.example.project.result.CodeMsg;
import com.example.project.result.Result;
import com.example.project.vo.GoodsVo;
import com.example.project.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.dc.pr.PRError;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/714:50
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private MiaoshaUserService miaoshaUserService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user, @RequestParam("orderId")long orderId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo order = orderService.getOrderById(orderId);
        if(order==null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }

}
