package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;

import java.util.Arrays;
import java.util.List;

@ApiModel("枚举值生成器")
public class CodeGenerator implements StringGenerator {

	private String code = "M,F";
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
