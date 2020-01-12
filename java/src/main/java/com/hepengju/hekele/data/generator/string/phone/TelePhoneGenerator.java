package com.hepengju.hekele.data.generator.string.phone;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;

/**
 * 电话号码
 * 
 * @author hepengju
 *
 */
public class TelePhoneGenerator implements StringGenerator {

	@Override
	public String generate() {
		String phoneArea = RandomUtil.randomOne(DataConst.telePhoneArea);
		int num = 11 - phoneArea.length();
		return phoneArea + "-" + RandomUtil.randomNum(DataConst.numbers, num );
	}

}
