package com.hepengju.hekele.data.generator.gen400_custom.gen410_name;


import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

@ApiModel("英文姓生成器") @Data
public class EnglishFirstNameGenerator extends AbstractStringGenerator {

	private String[] firstNameArray = DataConst.firstNames;
	
	@Override
	public String generate() {
		return firstNameArray[RandomUtils.nextInt(0, firstNameArray.length)];
	}

}
