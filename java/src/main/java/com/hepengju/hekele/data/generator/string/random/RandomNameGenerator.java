package com.hepengju.hekele.data.generator.string.random;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import lombok.Data;

@Data
public class RandomNameGenerator implements StringGenerator {
	
	private int min = 1;
	private int max = 4;

	@Override
	public String generate() {
		return RandomUtil.randomNum(DataConst.words, min, max);
	}

}
