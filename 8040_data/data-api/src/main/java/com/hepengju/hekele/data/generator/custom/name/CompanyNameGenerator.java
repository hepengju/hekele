package com.hepengju.hekele.data.generator.custom.name;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import io.swagger.annotations.ApiModel;

@ApiModel("公司名称生成器")
public class CompanyNameGenerator implements StringGenerator {

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
		
		String city = RandomUtil.randomOne(DataConst.cities).replace("省|市|区", "");
		result.append(city)
		      .append(RandomUtil.randomNum(DataConst.words, 2,4))
		      .append(RandomUtil.randomOne(DataConst.companyIndustries))
		      .append("有限公司");
		return result.toString();
	}

}
