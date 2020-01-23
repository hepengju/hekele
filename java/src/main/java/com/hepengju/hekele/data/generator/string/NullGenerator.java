package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.data.generator.Generator;
import io.swagger.annotations.ApiModel;

/**
 * 空生成器
 */
@ApiModel("空值生成器")
public class NullGenerator implements Generator<Object> {
    @Override
    public Object generate() {
        return null;
    }
}
