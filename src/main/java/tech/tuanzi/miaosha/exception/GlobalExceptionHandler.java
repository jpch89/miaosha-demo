package tech.tuanzi.miaosha.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.tuanzi.miaosha.vo.RespBean;
import tech.tuanzi.miaosha.vo.RespBeanEnum;

/**
 * 全局异常处理类
 * 注意：@RestControllerAdvice 相当于为类的每一个方法都加上了 @ResponseBody
 *
 * @author Patrick Ji
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public RespBean exceptionHandler(Exception e) {
        if (e instanceof GlobalException) {
            GlobalException ex = (GlobalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }

        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            RespBean respBean = RespBean.error(RespBeanEnum.BIND_ERROR);
            // 这里将 message 覆盖掉，变成更有用的信息
            respBean.setMessage("参数校验异常：" + ex.getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }

        return RespBean.error(RespBeanEnum.ERROR);
    }
}
