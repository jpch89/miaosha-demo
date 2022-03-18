package tech.tuanzi.miaosha.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.tuanzi.miaosha.entity.Order;
import tech.tuanzi.miaosha.entity.User;
import tech.tuanzi.miaosha.vo.GoodsVo;
import tech.tuanzi.miaosha.vo.OrderDetailVo;

/**
 * <p>
 * 订单服务类
 * </p>
 *
 * @author Patrick Ji
 */
public interface IOrderService extends IService<Order> {
    /**
     * 秒杀
     */
    Order miaosha(User user, GoodsVo goods);

    /**
     * 订单详情
     */
    OrderDetailVo detail(Long orderId);

    /**
     * 获取秒杀地址
     */
    String createPath(User user, Long goodsId);

    /**
     * 校验秒杀地址
     */
    boolean checkPath(User user, Long goodsId, String path);
}
