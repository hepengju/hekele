package com.hepengju.hekele.data.generator.string.random;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.StringGenerator;
import com.hepengju.hekele.data.generator.config.DataConst;

public class RandomNameGenerator implements StringGenerator {

	
	private int startInclusive = 1;
	private int endExclusive = 10;

	@Override
	public String generate() {
		return RandomUtil.randomNum(DataConst.words, startInclusive, endExclusive);
	}

}
