package com.example.project.Service;

import com.example.project.domain.MiaoshaOrder;
import com.example.project.domain.MiaoshaUser;
import com.example.project.domain.OrderInfo;
import com.example.project.vo.GoodsVo;

/**
 * @Title: project
 * @Package * @Description:     * @author CodingSir
 * @date 2020/9/713:41
 */
public interface OrderService {
    MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);

    OrderInfo getOrderById(long orderId);

    OrderInfo createOrder(MiaoshaUser user, GoodsVo goods);

    void deleteOrders();
}
