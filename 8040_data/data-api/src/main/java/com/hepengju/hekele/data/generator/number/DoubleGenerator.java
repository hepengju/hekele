package com.hepengju.hekele.data.generator.number;

import com.hepengju.hekele.data.generator.NumberGenerator;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;

@ApiModel("小数生成器")
@Data @NoArgsConstructor @AllArgsConstructor
public class DoubleGenerator implements NumberGenerator {

	private double min = 0.00;
	private double max = 1000.00;
	private int    scale = 2;
	
	@Override
	public Double generate() {
		double doubleValue = RandomUtils.nextDouble(min, max);
		return new BigDecimal(doubleValue).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
}
