package com.hepengju.hekele.data.generator.custom.name;


import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 中文姓名
 * 
 * @author hepengju
 *
 */
@Data @ApiModel("中文姓名生成器")
public class ChineseNameGenerator implements StringGenerator {

	private ChineseFirstNameGenerator firstName = new ChineseFirstNameGenerator();
	private ChineseLastNameGenerator  lastName  = new ChineseLastNameGenerator();
	
	@Override
	public String generate() {
		return firstName.generate() + lastName.generate();
	}

}
