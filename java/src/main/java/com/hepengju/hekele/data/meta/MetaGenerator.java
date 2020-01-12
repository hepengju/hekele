package com.hepengju.hekele.data.meta;

import com.hepengju.hekele.data.generator.Generator;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * 生成器的配置值
 */
@Data
public class MetaGenerator {

    private String generatorClassName;

    private String min;     // 最小值, 个人情况下表示字符串的最小长度
    private String max;     // 最大值, 个别情况下表示字符串的最大长度
    private String code;    // 代码值, 逗号分隔
    private int    scale;   // 保留小数位数

    public Generator toGenerator(){
        try {
            Generator generator = (Generator)Class.forName(generatorClassName).newInstance();
            BeanUtils.copyProperties(this, generator);
            return generator;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
