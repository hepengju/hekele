package com.hepengju.hekele.data.generator.gen300_string;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

@ApiModel("随机中文字生成器") @Data @Order(305)
public class RandomChineseGenerator extends AbstractStringGenerator {
	
	private int min = 2;
	private int max = 4;

	@Override
	public String generate() {
		return RandomUtil.randomNum(DataConst.words, min, max);
	}

}
