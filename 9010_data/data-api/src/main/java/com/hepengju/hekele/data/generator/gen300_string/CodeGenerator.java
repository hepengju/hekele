package com.hepengju.hekele.data.generator.gen300_string;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.Arrays;
import java.util.List;

@ApiModel("枚举值生成器") @Data @Order(302)
public class CodeGenerator extends AbstractStringGenerator {

	private boolean codeMulti;
	private String code = "H,M,L";
	private List<String> codeList;

	@Override
	public String generate() {
		return codeMulti ? RandomUtil.randomNumComma(codeList, 0, codeList.size()) : RandomUtil.randomOne(codeList);
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

	public CodeGenerator(String code, boolean codeMulti) {
		this.setCode(code);
		this.codeMulti = codeMulti;
	}
}
