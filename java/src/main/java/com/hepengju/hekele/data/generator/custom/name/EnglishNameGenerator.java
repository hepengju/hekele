package com.hepengju.hekele.data.generator.custom.name;


import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 英文姓名生成器
 * 
 * @author hepengju
 *
 */
@Data @ApiModel("英文姓名生成器")
public class EnglishNameGenerator implements StringGenerator {

	private EnglishFirstNameGenerator firstName = new EnglishFirstNameGenerator();
	private EnglishLastNameGenerator lastName = new EnglishLastNameGenerator();

	@Override
	public String generate() {
		return firstName.generate() + " " + lastName.generate();
	}
}
