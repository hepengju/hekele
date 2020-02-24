package com.hepengju.hekele.data.generator.gen400_custom.gen410_name;


import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

/**
 * 中文姓名
 * 
 * @author hepengju
 *
 */
@ApiModel("中文姓名生成器") @Order(412)
public class ChineseNameGenerator extends AbstractStringGenerator {

	private ChineseFirstNameGenerator firstName = new ChineseFirstNameGenerator();
	private ChineseLastNameGenerator  lastName  = new ChineseLastNameGenerator();
	
	@Override
	public String generate() {
		return firstName.generate() + lastName.generate();
	}

}
