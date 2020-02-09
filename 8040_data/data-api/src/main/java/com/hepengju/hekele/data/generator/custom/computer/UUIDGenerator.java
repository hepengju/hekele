package com.hepengju.hekele.data.generator.custom.computer;


import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;

import java.util.UUID;

/**
 * UUID生成器
 * 
 * @author hepengju
 *
 */
@ApiModel("UUID生成器")
public class UUIDGenerator implements StringGenerator {

	@Override
	public String generate() {
		return UUID.randomUUID().toString().replace("-","");
	}

}
