package com.hepengju.hekele.data.generator.number;

import com.hepengju.hekele.data.Generator;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;

/**
 * 小数生成器
 * 
 * @author hepengju
 *
 */
@Data
public class DoubleGenerator implements Generator<Double> {

	private Double startInclusive = 0.0;
	private Double endInclusive = 1000.0;
	private Integer scale = 2;
	
	@Override
	public Double generate() {
		double doubleValue = RandomUtils.nextDouble(startInclusive,endInclusive);
		return new BigDecimal(doubleValue).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

}
