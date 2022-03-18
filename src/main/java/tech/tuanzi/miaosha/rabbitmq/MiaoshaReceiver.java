package tech.tuanzi.miaosha.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tech.tuanzi.miaosha.entity.MiaoshaMessage;
import tech.tuanzi.miaosha.entity.MiaoshaOrder;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.service.IGoodsService;
import tech.tuanzi.miaosha.service.IOrderService;
import tech.tuanzi.miaosha.utils.JsonUtil;
import tech.tuanzi.miaosha.vo.GoodsVo;

/**
 * 秒杀消息接收者
 *
 * @author Patrick Ji
 */
@Service
@Slf4j
public class MiaoshaReceiver {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;

    /**
     * 下单操作
     */
    @RabbitListener(queues = "miaoshaQueue")
    public void receive(String message) {
        log.info("接收的消息：" + message);
        MiaoshaMessage miaoshaMessage = JsonUtil.jsonStr2Object(message, MiaoshaMessage.class);
        Long goodsId = miaoshaMessage.getGoodsId();
        User user = miaoshaMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(goodsId);

        // 判断库存
        if (goodsVo.getStockCount() < 1) {
            return;
        }

        // 判断是否重复抢购
        MiaoshaOrder miaoshaOrder =
                (MiaoshaOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (miaoshaOrder != null) {
            return;
        }

        // 下单操作
        orderService.miaosha(user, goodsVo);
    }
}
