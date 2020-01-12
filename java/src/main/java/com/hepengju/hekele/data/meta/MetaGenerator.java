package com.hepengju.hekele.data.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 生成器的配置值
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class MetaGenerator {

    private String min;     // 最小值, 个人情况下表示字符串的最小长度
    private String max;     // 最大值, 个别情况下表示字符串的最大长度
    private String code;    // 代码值, 逗号分隔
    private int    scale;   // 保留小数位数

}
