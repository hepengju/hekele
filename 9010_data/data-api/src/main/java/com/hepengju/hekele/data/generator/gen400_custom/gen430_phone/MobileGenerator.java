package com.hepengju.hekele.data.generator.gen400_custom.gen430_phone;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

/**
 * 手机号
 * 
 * @author hepengju
 *
 */
@ApiModel("手机号生成器") @Order(431)
public class MobileGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.mobileTypes) + RandomUtil.randomNum(DataConst.numbers, 8);
	}

}
