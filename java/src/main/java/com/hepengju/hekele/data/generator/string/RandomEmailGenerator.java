package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.RandomStringUtils;

@ApiModel("随机邮箱生成器")
public class RandomEmailGenerator implements StringGenerator {

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
		
        result.append(RandomStringUtils.randomAlphanumeric(3,10));
        result.append("@");
        result.append(RandomStringUtils.randomAlphanumeric(1,5));
        result.append(".");
        result.append(RandomStringUtils.randomAlphanumeric(2,3));

        return result.toString().toLowerCase();
	}

}
