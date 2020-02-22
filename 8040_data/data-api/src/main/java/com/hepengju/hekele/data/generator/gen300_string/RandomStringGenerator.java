package com.hepengju.hekele.data.generator.gen300_string;


import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.annotation.Order;

@ApiModel("随机字符生成器") @Data @Order(303)
public class RandomStringGenerator extends AbstractStringGenerator {

	private int min = 6;
	private int max = 6;

	@Override
	public String generate() {
		return RandomStringUtils.randomAlphabetic(min, max + 1);
	}
}
