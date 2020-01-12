package com.hepengju.hekele.data.generator.string.computer;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.StringGenerator;
import com.hepengju.hekele.data.generator.config.DataConst;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 邮箱生成器
 * 
 * @author hepengju
 */
public class EmailGenerator implements StringGenerator {

	private String[] popolarMailSupplies = DataConst.popolarMailSupplies;
	
	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
        result.append(RandomStringUtils.randomAlphanumeric(3,10));
        result.append(RandomUtil.randomOne(popolarMailSupplies));
        return result.toString().toLowerCase();
	}

}
