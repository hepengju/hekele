package com.hepengju.hekele.data.generator.custom.computer;

import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.RandomUtils;

/**
 * IP地址(v4)生成器
 * 
 * @author hepengju
 *
 */
@ApiModel("IPv4生成器")
public class IPv4Generator implements StringGenerator {

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
