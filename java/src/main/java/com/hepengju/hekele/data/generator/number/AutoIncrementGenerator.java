package com.hepengju.hekele.data.generator.number;


import com.hepengju.hekele.data.Generator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自增生成器
 * @author hepengju
 *
 */
public class AutoIncrementGenerator implements Generator<Integer> {

	private AtomicInteger atomic = new AtomicInteger(0);
	
	@Override
	public Integer generate() {
		return atomic.incrementAndGet();
	}

}
