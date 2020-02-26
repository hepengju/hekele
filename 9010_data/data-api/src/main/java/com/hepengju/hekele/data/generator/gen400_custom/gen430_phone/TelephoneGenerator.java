package com.hepengju.hekele.data.generator.gen400_custom.gen430_phone;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

/**
 * 电话号码
 * 
 * @author hepengju
 *
 */
@ApiModel("电话号码生成器") @Order(432)
public class TelephoneGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		String phoneArea = RandomUtil.randomOne(DataConst.telePhoneArea);
		int num = 11 - phoneArea.length();
		return phoneArea + "-" + RandomUtil.randomNum(DataConst.numbers, num );
	}

}
