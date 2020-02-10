package com.hepengju.hekele.data.annotation;

import com.hepengju.hekele.data.generator.string.RandomStringGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Generator {

    Class<? extends com.hepengju.hekele.data.generator.Generator> value() default RandomStringGenerator.class;
    String min()        default ""; // 最小值 (或最小长度)
    String max()        default ""; // 最大值 (或最大长度)
    int scale()         default 2 ; // 小数位数
    String code()       default ""; // 代码值: 逗号分隔
    boolean codeMulti() default false; // 20200210 代码值, 可以选择是否多选
}
