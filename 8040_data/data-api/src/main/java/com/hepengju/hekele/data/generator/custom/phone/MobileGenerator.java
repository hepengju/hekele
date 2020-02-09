package com.hepengju.hekele.data.generator.custom.phone;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import io.swagger.annotations.ApiModel;

/**
 * 手机号
 * 
 * @author hepengju
 *
 */
@ApiModel("手机号生成器")
public class MobileGenerator implements StringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.mobileTypes) + RandomUtil.randomNum(DataConst.numbers, 8);
	}

}