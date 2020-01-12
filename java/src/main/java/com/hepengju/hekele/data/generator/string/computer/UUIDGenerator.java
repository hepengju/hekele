package com.hepengju.hekele.data.generator.string.computer;


import com.hepengju.hekele.data.StringGenerator;

import java.util.UUID;

/**
 * UUID生成器
 * 
 * @author hepengju
 *
 */
public class UUIDGenerator implements StringGenerator {

	@Override
	public String generate() {
		return UUID.randomUUID().toString().replace("-","");
	}

}
