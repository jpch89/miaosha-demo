package tech.tuanzi.miaosha.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.tuanzi.miaosha.entity.User;

/**
 * 详情返回对象
 *
 * @author Patrick Ji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private User user;
    private GoodsVo goodsVo;
    private int miaoshaStatus;
    private int remainSeconds;
}
