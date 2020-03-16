package com.hepengju.hekele.data.generator.gen400_custom.gen440_computer;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen410_name.ChineseNamePinyinGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen410_name.EnglishNameGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen430_phone.MobileGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

/**
 * 邮箱生成器
 *
 * <pre>
 *     中文姓名拼音.xxx.com     40%
 *	   手机号/QQ号.xxx.com     40%
 *	   英文姓名.xxx.com        15%
 *	   随机字母.xxx.com        5%
 *
 * </pre>
 * 
 * @author hepengju
 */
@ApiModel("邮箱生成器") @Data @Order(441)
public class EmailGenerator extends AbstractStringGenerator {

	private static List<String> sepList = Arrays.asList("","_", ".");
	private String[] mailSupplies = DataConst.mailSupplies;
	private ChineseNamePinyinGenerator pinyinGenerator = new ChineseNamePinyinGenerator();
	private EnglishNameGenerator englishNameGenerator = new EnglishNameGenerator();
	private MobileGenerator mobileGenerator = new MobileGenerator();

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();

		double chance01 = Math.random();
		String separator = RandomUtil.randomOne(sepList);

		if (chance01 < 0.4) {
			result.append(pinyinGenerator.generate().toLowerCase().replace(" ", separator));
		} else if (chance01 < 0.8) {
			result.append(mobileGenerator.generate());
		} else if (chance01 < 0.95) {
			result.append(englishNameGenerator.generate().toLowerCase().replace(" ", separator));
		} else {
			result.append(RandomStringUtils.randomAlphanumeric(6,10));
		}

        result.append(RandomUtil.randomOne(mailSupplies));
        return result.toString().toLowerCase();
	}

}
