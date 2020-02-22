package com.hepengju.hekele.data.generator.gen400_custom.gen460_address;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.annotation.Order;

@ApiModel("中国地址生成器") @Order(461)
public class ChinaAddressGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
		
		//省市
		String provinceCity = RandomUtil.randomOne(DataConst.provinceCities);
		double chance01 = Math.random();

		//去掉省
		if (chance01 < 0.2) {
			provinceCity = provinceCity.replaceFirst("省", "");

			//去掉市
		} else if (chance01 < 0.4) {
			provinceCity = provinceCity.replaceFirst("市", "");

			//去掉省和市
		} else if (chance01 < 0.6) {
			provinceCity = provinceCity.replaceFirst("省", "").replaceFirst("市", "");

			//没有省,直接取xxx市
		} else if (chance01 < 0.8) {
			provinceCity = RandomUtil.randomOne(DataConst.cities);

			//省市都没有
		} else {
			provinceCity = "";
		}
		
		result.append(provinceCity);
		
		//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		double chance02 = Math.random();
		//xxx路xxx号xxx小区xxx单元xxx室
		if(chance02 < 0.2) {
			result.append(RandomUtil.randomNum(DataConst.words, 2, 3) + "路")
			      .append(RandomUtils.nextInt(1, 8000) + "号")
			      .append(RandomUtil.randomNum(DataConst.words, 2, 4) + "小区")
			      .append(RandomUtils.nextInt(1, 20) + "单元")
			      .append(RandomUtils.nextInt(101, 2500) + "室")
		          ;
			
		//xxx路xxx地标
		}else if(chance02 < 0.4) {
			result.append(RandomUtil.randomNum(DataConst.words, 2, 3) + "路")
			      .append(RandomUtil.randomNum(DataConst.words, 2, 4) + RandomUtil.randomOne(DataConst.addressFlags));
			
		//xxx路xxx号
		}else if(chance02 < 0.6) {
			result.append(RandomUtil.randomNum(DataConst.words, 2, 3) + "路")
		          .append(RandomUtils.nextInt(1, 8000) + "号");
			
		//xxx路与xxx路交叉口
		}else if(chance02 < 0.8) {
			result.append(RandomUtil.randomNum(DataConst.words, 2, 3) + "路")
	              .append(RandomUtil.randomNum(DataConst.words, 2, 3) + "路")
	              .append("交叉口");
			
		//xxx街道xxx号
		}else {
			result.append(RandomUtil.randomNum(DataConst.words, 2, 3) + "街道")
	              .append(RandomUtils.nextInt(1, 8000) + "号");
		}
		return result.toString();
	}

}
