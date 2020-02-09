package com.hepengju.hekele.data.generator.custom.password;


import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * md5密码生成器
 *
 * @author hepengju
 */
@ApiModel("Md5密码生成器")
public class Md5Generator implements StringGenerator {

	@Override
	public String generate() {
		String password = RandomStringUtils.randomAlphabetic(6, 10);
		return DigestUtils.md5Hex(password);
	}

}
