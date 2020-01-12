package com.hepengju.hekele.data.generator.number;

import com.hepengju.hekele.data.Generator;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

/**
 * 整数生成器
 * 
 * @author hepengju
 *
 */
@Data
public class IntegerGenerator implements Generator<Integer> {

	private Integer startInclusive = 0;
	private Integer endExclusive = 99999;
	
	@Override
	public Integer generate() {
		return RandomUtils.nextInt(startInclusive,endExclusive);
	}
}
