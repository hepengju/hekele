package com.hepengju.hekele.data.generator.string.name;


import com.hepengju.hekele.data.StringGenerator;
import com.hepengju.hekele.data.generator.config.DataConst;
import org.apache.commons.lang3.RandomUtils;

public class EnglishFirstNameGenerator implements StringGenerator {

	private String[] firstNameArray = DataConst.firstNames;
	
	@Override
	public String generate() {
		return firstNameArray[RandomUtils.nextInt(0, firstNameArray.length)];
	}

}
