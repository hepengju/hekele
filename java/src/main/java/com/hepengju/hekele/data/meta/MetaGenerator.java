package com.hepengju.hekele.data.meta;

import lombok.Data;

import java.util.List;

/**
 * 生成器的配置值
 */
@Data
public class MetaGenerator {

    private String min;     // 最小值, 个人情况下表示字符串的最小长度
    private String max;     // 最大值, 个别情况下表示字符串的最大长度
    private int    scale;   // 保留小数位数
    private String length;  // 字符串的最大长度

    private List<String> codeList; // 代码值列表
}
