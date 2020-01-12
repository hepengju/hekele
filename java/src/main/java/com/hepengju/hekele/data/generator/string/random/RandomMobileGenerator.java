package com.hepengju.hekele.data.generator.string.random;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;

/**
 * 手机号
 * 
 * @author hepengju
 *
 */
public class RandomMobileGenerator implements StringGenerator {

	@Override
	public String generate() {
		return "1" + RandomUtil.randomNum(DataConst.numbers, 10);
	}

}
