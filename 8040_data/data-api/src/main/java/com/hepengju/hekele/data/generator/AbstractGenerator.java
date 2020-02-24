package com.hepengju.hekele.data.generator;

import lombok.Data;

/**
 * 抽象类: 共同的属性
 *
 * @author hepengju
 */
@Data
public abstract class AbstractGenerator<T> implements Generator<T> {

    private String prefix; // 前缀
    private String suffix; // 后缀

}
