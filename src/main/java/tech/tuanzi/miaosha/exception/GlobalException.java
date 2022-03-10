package tech.tuanzi.miaosha.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.tuanzi.miaosha.vo.RespBeanEnum;

/**
 * 全局异常
 *
 * @author Patrick Ji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    private RespBeanEnum respBeanEnum;
}
