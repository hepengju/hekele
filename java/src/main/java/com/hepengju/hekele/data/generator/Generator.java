package com.hepengju.hekele.data.generator;

/**
 * 数据生成器
 * 
 * <br> 具有生成测试数据的功能
 *
 * @author hepengju
 *
 */
@FunctionalInterface
public interface Generator<T>{
	T generate();
}
