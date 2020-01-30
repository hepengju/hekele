package com.hepengju.hekele.data.generator.custom.address;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import io.swagger.annotations.ApiModel;

@ApiModel("中国省份生成器")
public class ChinaProvinceGenerator implements StringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.provinces);
	}

}
