package com.hepengju.hekele.data.generator.gen300_string;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("随机手机号生成器") @Order(306)
public class RandomMobileGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		return "1" + RandomUtil.randomNum(DataConst.numbers, 10);
	}

}
