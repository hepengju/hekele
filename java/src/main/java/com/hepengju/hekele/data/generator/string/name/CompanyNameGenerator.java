package com.hepengju.hekele.data.generator.string.name;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.StringGenerator;
import com.hepengju.hekele.data.generator.config.DataConst;

public class CompanyNameGenerator implements StringGenerator {

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
		
		String city = RandomUtil.randomOne(DataConst.cities);
		city = city.replace("省", "").replace("市", "").replace("区", "");
		
		result.append(city)
		      .append(RandomUtil.randomNum(DataConst.words, 2,4))
		      .append(RandomUtil.randomOne(DataConst.companyIndustries))
		      .append("有限公司");
		
		return result.toString();
	}

}
