package com.hepengju.hekele.data.generator.string.random;


import com.hepengju.hekele.data.generator.StringGenerator;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class RandomStringGenerator implements StringGenerator {

	private int min = 0;
	private int max = 64;

	@Override
	public String generate() {
		return RandomStringUtils.randomAlphanumeric(min, max + 1);
	}
}
