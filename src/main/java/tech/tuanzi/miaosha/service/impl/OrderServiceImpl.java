package tech.tuanzi.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.tuanzi.miaosha.entity.Order;
import tech.tuanzi.miaosha.mapper.OrderMapper;
import tech.tuanzi.miaosha.service.IOrderService;

/**
 * <p>
 * 订单服务实现类
 * </p>
 *
 * @author Patrick Ji
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
