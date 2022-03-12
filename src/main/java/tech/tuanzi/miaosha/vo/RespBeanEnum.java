package tech.tuanzi.miaosha.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 公共返回对象枚举
 *
 * @author Patrick Ji
 */
@ToString
@Getter
@AllArgsConstructor
public enum RespBeanEnum {
    // 通用
    // 成功
    SUCCESS(200, "SUCCESS"),
    // 错误
    ERROR(500, "服务端异常"),

    // 登录模块：5002xx
    // 用户名或密码错误
    LOGIN_ERROR(500210, "用户名或密码不正确"),
    // 手机号码格式不正确
    MOBILE_ERROR(500211, "手机号码格式不正确"),
    // 参数校验异常
    BIND_ERROR(500212, "参数校验异常"),

    // 秒杀模块：5005xx
    EMPTY_STOCK(500500, "库存不足");

    private final Integer code;
    private final String message;
}
