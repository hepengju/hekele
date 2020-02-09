package com.hepengju.hekele.data.generator.custom.computer;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 邮箱生成器
 * 
 * @author hepengju
 */
@Data @ApiModel("邮箱生成器")
public class EmailGenerator implements StringGenerator {

	private String[] mailSupplies = DataConst.mailSupplies;
	
	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
        result.append(RandomStringUtils.randomAlphanumeric(3,10));
        result.append(RandomUtil.randomOne(mailSupplies));
        return result.toString().toLowerCase();
	}

}
