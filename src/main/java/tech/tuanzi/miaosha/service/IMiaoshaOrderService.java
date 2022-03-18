package tech.tuanzi.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.tuanzi.miaosha.entity.MiaoshaOrder;
import tech.tuanzi.miaosha.entity.User;

/**
 * <p>
 * 秒杀订单服务类
 * </p>
 *
 * @author Patrick Ji
 */
public interface IMiaoshaOrderService extends IService<MiaoshaOrder> {

    /**
     * 获取秒杀结果
     *
     * @return 如果返回 orderId 表示秒杀成功；返回 -1 表示秒杀失败，返回 0 表示排队中
     */
    Long getResult(User user, Long goodsId);
}
