package com.hepengju.hekele.data.generator.string.phone;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.config.DataConst;

/**
 * 手机号
 * 
 * @author hepengju
 *
 */
public class MobileGenerator implements StringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.mobileTypes) + RandomUtil.randomNum(DataConst.numbers, 8);
	}

}
