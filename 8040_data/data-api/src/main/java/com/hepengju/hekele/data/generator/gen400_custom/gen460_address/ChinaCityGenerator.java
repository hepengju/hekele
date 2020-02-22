package com.hepengju.hekele.data.generator.gen400_custom.gen460_address;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("中国城市生成器") @Order(462)
public class ChinaCityGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.cities);
	}

}
