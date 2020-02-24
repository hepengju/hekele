package com.hepengju.hekele.data.generator.gen400_custom.gen440_computer;


import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

import java.util.UUID;

/**
 * UUID生成器
 * 
 * @author hepengju
 *
 */
@ApiModel("UUID生成器") @Order(443)
public class UUIDGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		return UUID.randomUUID().toString().replace("-","");
	}

}
