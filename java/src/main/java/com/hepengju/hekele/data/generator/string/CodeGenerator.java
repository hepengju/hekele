package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;

import java.util.Arrays;
import java.util.List;

/**
 * 代码列表
 * 
 * @author hepengju
 *
 */
public class CodeGenerator implements StringGenerator {

	private String code = "";
	private List<String> codeList;

	@Override
	public String generate() {
		return RandomUtil.randomOne(codeList);
	}

	public void setCode(String code) {
		this.code = code;
		this.codeList = Arrays.asList(code.split(","));
	}

	public CodeGenerator() {
		this.setCode(this.code);
	}

	public CodeGenerator(String code) {
		this.setCode(code);
	}
}
