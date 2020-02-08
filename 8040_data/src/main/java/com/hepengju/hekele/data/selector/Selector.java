package com.hepengju.hekele.data.selector;


import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.meta.MetaColumn;

import java.util.List;

/**
 * 
 * 数据产生器的选择器
 * 
 * @author hepengju
 *
 */
@FunctionalInterface
public interface Selector {
	
	/**
	 * @param metaColumn  列元数据
	 * @param sampleData  样例数据
	 */
	Generator<?> select(MetaColumn metaColumn, List<Object> sampleData);
}
