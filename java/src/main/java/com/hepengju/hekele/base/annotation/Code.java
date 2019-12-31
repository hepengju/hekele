package com.hepengju.hekele.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 代码枚举值注解
 *
 * <br> 应用在BO类的字段上,在返回list方法时带上对应的code枚举值
 * <br> 默认值此处写的是"",实际处理时如果没有指定value值则采用字段名
 *
 * <br> 注意:
 * <br> 1.BO类属性名上注解@Code指定的代码类型名称与代码表中的type_code一致,若两者一致@Code后的代码名称可省.不一致时，需明确指定.
 * <br> 2.返回结果中代码类型与条目类型与代码表中保持完全一致,不做任何转换(即不会做下划线转换)
 * <br>
 *
 * @see com.hepengju.hekele.base.core.R
 * @see com.hepengju.hekele.base.core.HeService
 *
 * @author hepengju 2018-04-09
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Code {

}