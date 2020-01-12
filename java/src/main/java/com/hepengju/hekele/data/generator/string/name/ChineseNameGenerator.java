package com.hepengju.hekele.data.generator.string.name;


import com.hepengju.hekele.data.generator.StringGenerator;
import lombok.Data;

/**
 * 中文姓名
 * 
 * @author hepengju
 *
 */
@Data
public class ChineseNameGenerator implements StringGenerator {

	private ChineseFirstNameGenerator firstName = new ChineseFirstNameGenerator();
	private ChineseLastNameGenerator  lastName  = new ChineseLastNameGenerator();
	
	@Override
	public String generate() {
		return firstName.generate() + lastName.generate();
	}

}
