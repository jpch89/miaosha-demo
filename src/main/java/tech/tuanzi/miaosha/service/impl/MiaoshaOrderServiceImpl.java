package tech.tuanzi.miaosha.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.tuanzi.miaosha.entity.MiaoshaOrder;
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

}
