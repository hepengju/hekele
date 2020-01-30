package com.hepengju.hekele.data.meta;

import com.hepengju.hekele.data.generator.Generator;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.constraints.NotNull;
import java.lang.reflect.InvocationTargetException;

/**
 * 生成器的配置值
 */
@Data
public class MetaGenerator {

    @NotNull
    private String className;

    private String  min;     // 最小值, 个人情况下表示字符串的最小长度
    private String  max;     // 最大值, 个别情况下表示字符串的最大长度
    private String  code;    // 代码值, 逗号分隔
    private Integer scale;   // 保留小数位数

    public Generator toGenerator(){
        try {
            Generator generator = (Generator)Class.forName(className).newInstance();
            if(min   != null) BeanUtils.copyProperty(generator, "min"  , min);
            if(max   != null) BeanUtils.copyProperty(generator, "max"  , max);
            if(code  != null) BeanUtils.copyProperty(generator, "code" , code);
            if(scale != null) BeanUtils.copyProperty(generator, "scale", scale);
            return generator;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
