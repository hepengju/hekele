package com.hepengju.hekele.data.generator.gen300_string;


import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.annotation.Order;

@ApiModel("随机邮箱生成器") @Data @Order(307)
public class RandomEmailGenerator extends AbstractStringGenerator {

    private int min = 6;
    private int max = 9;

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
		
        result.append(RandomStringUtils.randomAlphanumeric(min,max));
        result.append("@");
        result.append(RandomStringUtils.randomAlphanumeric(1,5));
        result.append(".");
        result.append(RandomStringUtils.randomAlphanumeric(2,3));

        return result.toString().toLowerCase();
	}

}
