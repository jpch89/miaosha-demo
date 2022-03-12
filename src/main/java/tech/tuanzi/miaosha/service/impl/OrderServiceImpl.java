package tech.tuanzi.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.tuanzi.miaosha.entity.MiaoshaGoods;
import tech.tuanzi.miaosha.entity.MiaoshaOrder;
import tech.tuanzi.miaosha.entity.Order;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.mapper.OrderMapper;
import tech.tuanzi.miaosha.service.IMiaoshaGoodsService;
import tech.tuanzi.miaosha.service.IMiaoshaOrderService;
import tech.tuanzi.miaosha.service.IOrderService;
import tech.tuanzi.miaosha.vo.GoodsVo;

import java.util.Date;

/**
 * <p>
 * 订单服务实现类
 * </p>
 *
 * @author Patrick Ji
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private IMiaoshaGoodsService miaoshaGoodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private IMiaoshaOrderService miaoshaOrderService;

    /**
     * 秒杀
     */
    @Override
    public Order miaosha(User user, GoodsVo goods) {
        // 秒杀商品表减库存
        MiaoshaGoods miaoshaGoods = miaoshaGoodsService.getOne(
                new QueryWrapper<MiaoshaGoods>().eq("goods_id", goods.getId())
        );
        miaoshaGoods.setStockCount(miaoshaGoods.getStockCount() - 1);
        miaoshaGoodsService.updateById(miaoshaGoods);

        // 生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(miaoshaGoods.getMiaoshaPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        // 生成秒杀订单
        // 为什么要先生成订单，再生成秒杀订单？
        // 因为秒杀订单中有一个订单 id 需要和订单相关联
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setUserId(user.getId());
        // order 插入之后会返回主键，所以可以直接使用
        miaoshaOrder.setOrderId(order.getId());
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrderService.save(miaoshaOrder);

        return order;
    }
}
