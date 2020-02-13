package com.hepengju.hekele.base.util;

import com.hepengju.hekele.base.core.exception.BeanValidException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * 验证工具类
 *
 * <pre>
 *  常用的约束:
 *      - 空与非空: @Null @NotNull @NotBlank @NotEmpty
 *      - Boolean: @AssertTrue @AssertFalse
 *      - 日期: @Future @FutureOrPresent @Past @PastOrPresent
 *      - 数值: @Max @Min @DecimalMax @DecimalMin @Negative @NegativeOrZero @Positive @PositiveOrZero @Digits(integer = 3, fraction = 2)
 *      - 其他: @Pattern @Email @Size
 *      - hibernate扩展: @Length @Range @URL
 *  推荐使用: @NotNull @NotBlank @Length @Size
 *
 * <br> JSR303, JSR349, JSR380
 * <br> <a href="https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-declaring-bean-constraints">
 * Hibernate Validator - Jakarta Bean验证参考实现
 * </a>
 * <br> <a href="https://blog.csdn.net/f641385712/article/details/96638596">深入了解数据校验</a>
 * <br> <a href="https://www.cnblogs.com/leeego-123/p/10820099.html">springboot使用hibernate validator校验</a>
 * <br> <a href="https://www.jianshu.com/p/3267689ebf1b">优雅的做数据校验-Hibernate Validator</a>
 * </pre>
 *
 * @author he_pe 2019-12-22
 */
public class ValidUtil {

    private static Validator validator;

    // 静态代码块初始化 validator
    static {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true)
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    /**
     * 验证Bean
     */
    public static <T> void validateBean(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        handleViolations(violations);
    }

    /**
     * 验证Bean的属性
     */
    public static <T> void validateProperty(T object, String propertyName, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validateProperty(object, propertyName, groups);
        handleViolations(violations);
    }

    /**
     * 处理违反规则
     */
    private static <T> void handleViolations(Set<ConstraintViolation<T>> violations) {
        if (violations == null || violations.size() == 0) return;
        ConstraintViolation<T> violation = violations.iterator().next();
        String errorMessage = getErrorMessage(violation);
        throw new BeanValidException(errorMessage);
    }

    /**
     * 违反规则 --> 自定义的提示信息
     */
    public static <T> String getErrorMessage(ConstraintViolation<T> violation) {
        Class<T> beanClass = violation.getRootBeanClass();
        String className = StringUtils.uncapitalize(beanClass.getSimpleName());
        String fieldName = violation.getPropertyPath().toString();

        String errorMessage = className + "." + fieldName; //role.roleCode

        // 尝试转换为中文名
        try {
            String apiModelValue = BOUtil.getApiModelValue(beanClass);
            String apiModelPropertyValue = BOUtil.getApiModelPropertyValue(beanClass, fieldName);
            if (StringUtils.isNoneBlank(apiModelValue, apiModelPropertyValue)) {
                errorMessage = apiModelValue + "的" + apiModelPropertyValue ; //角色的角色代码
            }
        } catch (Exception e) {
        }

        errorMessage = errorMessage + violation.getMessage();
        return errorMessage;
    }
}
