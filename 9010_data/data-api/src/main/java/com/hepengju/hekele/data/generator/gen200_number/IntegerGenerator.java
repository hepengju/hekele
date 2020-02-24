package com.hepengju.hekele.data.generator.gen200_number;

import com.hepengju.hekele.data.generator.AbstractNumberGenerator;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.annotation.Order;

@ApiModel("整数生成器") @Data @NoArgsConstructor @AllArgsConstructor @Order(201)
public class IntegerGenerator extends AbstractNumberGenerator<Integer> {

	private int min = 0;
	private int max = 99999;
	private String format = "#";

	@Override
	public Integer generate() {
		return RandomUtils.nextInt(min, max);
	}
}
