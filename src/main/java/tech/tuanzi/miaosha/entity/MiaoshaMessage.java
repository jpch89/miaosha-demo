package tech.tuanzi.miaosha.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒杀信息类
 *
 * @author Patrick Ji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiaoshaMessage {
    private User user;
    private Long goodsId;
}
