package com.hepengju.hekele.data.generator.number;


import com.hepengju.hekele.data.generator.NumberGenerator;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自增生成器
 * @author hepengju
 *
 */
public class AutoIncrementGenerator implements NumberGenerator {

	@Getter @Setter
	private int min = 0;
	private AtomicInteger atomic;

	public AutoIncrementGenerator() {}
	public AutoIncrementGenerator(int min) { this.min = min; }

	@Override
	public Integer generate() {
		if(atomic == null) atomic = new AtomicInteger(min);
		return atomic.incrementAndGet();
	}

}
