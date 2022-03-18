package tech.tuanzi.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import tech.tuanzi.miaosha.entity.MiaoshaGoods;
import tech.tuanzi.miaosha.entity.MiaoshaOrder;
import tech.tuanzi.miaosha.entity.Order;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.exception.GlobalException;
import tech.tuanzi.miaosha.mapper.OrderMapper;
import tech.tuanzi.miaosha.service.IGoodsService;
import tech.tuanzi.miaosha.service.IMiaoshaGoodsService;
import tech.tuanzi.miaosha.service.IMiaoshaOrderService;
import tech.tuanzi.miaosha.service.IOrderService;
import tech.tuanzi.miaosha.utils.MD5Util;
import tech.tuanzi.miaosha.utils.UUIDUtil;
import tech.tuanzi.miaosha.vo.GoodsVo;
import tech.tuanzi.miaosha.vo.OrderDetailVo;
import tech.tuanzi.miaosha.vo.RespBeanEnum;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 秒杀
     */
    @Transactional
    @Override
    public Order miaosha(User user, GoodsVo goods) {
        // 秒杀商品表减库存
        MiaoshaGoods miaoshaGoods = miaoshaGoodsService.getOne(
                new QueryWrapper<MiaoshaGoods>().eq("goods_id", goods.getId())
        );
        miaoshaGoods.setStockCount(miaoshaGoods.getStockCount() - 1);
        boolean result = miaoshaGoodsService.update(new UpdateWrapper<MiaoshaGoods>()
                .setSql("stock_count = stock_count - 1")
                .eq("goods_id", goods.getId())
                .gt("stock_count", 0)
        );

        // 更新失败，直接返回空
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (miaoshaGoods.getStockCount() < 1) {
            // 判断是否还有库存
            valueOperations.set("isStockEmpty:" + goods.getId(), "0");
            return null;
        }

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
        // 使用 Redis 保存秒杀订单
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + goods.getId(), miaoshaOrder);
        return order;
    }

    /**
     * 订单详情
     */
    @Override
    public OrderDetailVo detail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setOrder(order);
        detail.setGoodsVo(goodsVo);
        return detail;
    }

    /**
     * 获取秒杀地址
     */
    @Override
    public String createPath(User user, Long goodsId) {
        String path = MD5Util.md5(UUIDUtil.uuid() + "123456");
        // 这种随机生成的地址，不会保存很长时间，所以存到 Redis 里面
        redisTemplate.opsForValue().set("miaoshaPath:" + user.getId() + ":" + goodsId, path, 60, TimeUnit.SECONDS);
        return path;
    }

    @Override
    public boolean checkPath(User user, Long goodsId, String path) {
        if (null == user || goodsId < 0 || StringUtils.isEmpty(path)) {
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("miaoshaPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }
}
