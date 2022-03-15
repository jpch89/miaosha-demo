package tech.tuanzi.miaosha.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.tuanzi.miaosha.entity.Order;

/**
 * 订单详情返回对象
 *
 * @author Patrick Ji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailVo {
    private Order order;
    private GoodsVo goodsVo;
}
