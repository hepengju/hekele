package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data @ApiModel("随机字符数字生成器")
public class RandomStringNumberGenerator implements StringGenerator {

	private int min = 6;
	private int max = 10;

	@Override
	public String generate() {
		return RandomStringUtils.randomNumeric(min, max + 1);
	}
}
