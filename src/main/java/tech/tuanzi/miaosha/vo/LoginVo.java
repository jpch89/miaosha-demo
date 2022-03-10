package tech.tuanzi.miaosha.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tech.tuanzi.miaosha.validator.IsMobile;

import javax.validation.constraints.NotNull;

/**
 * 登录参数
 *
 * @author Patrick Ji
 */
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 6)
    private String password;
}
