package tech.tuanzi.miaosha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tech.tuanzi.miaosha.MiaoshaApplication;
import tech.tuanzi.miaosha.entity.MiaoshaOrder;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.mapper.MiaoshaOrderMapper;
import tech.tuanzi.miaosha.service.IMiaoshaOrderService;

/**
 * <p>
 * 秒杀订单服务实现类
 * </p>
 *
 * @author Patrick Ji
 */
@Service
public class MiaoshaOrderServiceImpl extends ServiceImpl<MiaoshaOrderMapper, MiaoshaOrder> implements IMiaoshaOrderService {
    @Autowired
    private MiaoshaOrderMapper miaoshaOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取秒杀结果
     *
     * @return 如果返回 orderId 表示秒杀成功；返回 -1 表示秒杀失败，返回 0 表示排队中
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        // 弹幕说：直接查缓存就行了，这样还是去查库了
        // 弹幕又说：这里没有必要查 Redis，因为只有排队的才会轮询，其他人已经返回空库存，不会轮询
        MiaoshaOrder miaoshaOrder = miaoshaOrderMapper.selectOne(
                new QueryWrapper<MiaoshaOrder>().eq("user_id", user.getId()).eq("goods_id", goodsId)
        );
        if (null != miaoshaOrder) {
            return miaoshaOrder.getOrderId();
        }
        if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
            return -1L;
        }
        return 0L;
    }
}
