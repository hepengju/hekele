package com.hepengju.hekele.data.generator.string.name;


import com.hepengju.hekele.data.StringGenerator;

/**
 * 英文姓名生成器
 * 
 * @author hepengju
 *
 */
public class EnglishNameGenerator implements StringGenerator {

	private EnglishFirstNameGenerator firstName = new EnglishFirstNameGenerator();
	private EnglishLastNameGenerator  lastName  = new EnglishLastNameGenerator();
	
	@Override
	public String generate() {
		return firstName.generate() + " " + lastName.generate();
	}
}
