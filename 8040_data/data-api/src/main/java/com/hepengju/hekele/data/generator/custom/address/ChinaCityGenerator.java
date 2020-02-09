package com.hepengju.hekele.data.generator.custom.address;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import io.swagger.annotations.ApiModel;

@ApiModel("中国城市生成器")
public class ChinaCityGenerator implements StringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.cities);
	}

}
