package com.hepengju.hekele.data.generator.gen300_string;


import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.annotation.Order;

@ApiModel("随机数字生成器") @Data @Order(304)
public class RandomNumberGenerator extends AbstractStringGenerator {

	private int min = 6;
	private int max = 6;

	@Override
	public String generate() {
		return RandomStringUtils.randomNumeric(min, max + 1);
	}
}
