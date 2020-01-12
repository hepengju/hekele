package com.hepengju.hekele.data.generator.string.address;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;

/**
 * 省份
 * 
 * @author hepengju
 */
public class ProvinceGenerator implements StringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.provinces);
	}

}
