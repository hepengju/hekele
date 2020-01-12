package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.StringGenerator;
import lombok.Data;

import java.util.List;

/**
 * 代码列表
 * 
 * @author hepengju
 *
 */
@Data
public class CodeListGenerator implements StringGenerator {

	private List<String> codeList;
	
	public CodeListGenerator(List<String> codeList) {
		super();
		this.codeList = codeList;
	}

	@Override
	public String generate() {
		return RandomUtil.randomOne(codeList);
	}

}
