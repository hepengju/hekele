package com.hepengju.hekele.data.generator.gen400_custom.gen440_computer;

import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.annotation.Order;

/**
 * IP地址(v4)生成器
 * 
 * @author hepengju
 *
 */
@ApiModel("IPv4生成器") @Order(442)
public class IPv4Generator extends AbstractStringGenerator {

	@Override
	public String generate() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(RandomUtils.nextInt(0, 255))
		  .append(".")
		  .append(RandomUtils.nextInt(0, 255))
		  .append(".")
		  .append(RandomUtils.nextInt(0, 255))
		  .append(".")
		  .append(RandomUtils.nextInt(0, 255));
		
		return sb.toString();
	}

}
