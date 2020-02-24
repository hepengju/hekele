package com.hepengju.hekele.data.generator.gen400_custom.gen420_password;


import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.annotation.Order;

/**
 * md5密码生成器
 *
 * @author hepengju
 */
@ApiModel("Md5密码生成器") @Order(422)
public class Md5Generator extends AbstractStringGenerator {

	@Override
	public String generate() {
		String password = RandomStringUtils.randomAlphabetic(6, 10);
		return DigestUtils.md5Hex(password);
	}

}
