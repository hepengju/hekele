package com.hepengju.hekele.data.generator;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据生成器
 * 
 * <br> 具有生成测试数据的功能
 *
 * @author hepengju
 *
 */
@Component
@FunctionalInterface
public interface Generator<T>{

	/**
	 * 生成数据
	 */
	T generate();

	/**
	 * 生成数据列表
	 */
	default List<T> generateList(int size){
		List<T> sampleData = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			sampleData.add(this.generate());
		}
		return sampleData;
	}

	/**
	 * 生成数据列表-不重复
	 */
	default Set<T> generateSet(int size){
		Set<T> sampleData = new HashSet<>();
		for (int i = 0; i < size; i++) {
			sampleData.add(this.generate());
		}

		while (sampleData.size() < size) {
			sampleData.add(this.generate());
		}
		return sampleData;
	}

}
