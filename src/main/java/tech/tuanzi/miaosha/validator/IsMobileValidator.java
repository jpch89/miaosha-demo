package tech.tuanzi.miaosha.validator;

import org.thymeleaf.util.StringUtils;
import tech.tuanzi.miaosha.utils.ValidatorUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 手机号码校验规则
 *
 * @author Patrick Ji
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {
    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (required) {
            return ValidatorUtil.isMobile(value);
        }
        // 非必填时，如果什么都没有填，默认验证通过
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        // 非必填时，如果填了东西，使用 ValidatorUtil 校验
        return ValidatorUtil.isMobile(value);
    }
}
