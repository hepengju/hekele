package com.hepengju.hekele.data.generator.gen300_string;


import com.hepengju.hekele.data.generator.Generator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("空值生成器") @Order(301)
public class NullGenerator implements Generator<Object> {
    @Override
    public Object generate() {
        return null;
    }
}
