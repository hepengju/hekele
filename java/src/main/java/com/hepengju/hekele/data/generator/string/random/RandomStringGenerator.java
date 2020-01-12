package com.hepengju.hekele.data.generator.string.random;


import com.hepengju.hekele.data.StringGenerator;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomStringGenerator implements StringGenerator {

	private int maxLength = 64;
	

	@Override
	public String generate() {
		return RandomStringUtils.randomAlphanumeric(0,maxLength + 1);
	}
}
