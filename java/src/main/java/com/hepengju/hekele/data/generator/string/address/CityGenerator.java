package com.hepengju.hekele.data.generator.string.address;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;

/**
 * 城市
 * 
 * @author hepengju
 */
public class CityGenerator implements StringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.cities);
	}

}
