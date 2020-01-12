package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.data.generator.Generator;

/**
 * 空生成器
 */
public class NullGenerator implements Generator<Object> {
    @Override
    public Object generate() {
        return null;
    }
}
