package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 代码列表
 * 
 * @author hepengju
 *
 */
@Data @AllArgsConstructor
public class CodeListGenerator implements StringGenerator {

	private List<String> codeList;

	@Override
	public String generate() {
		return RandomUtil.randomOne(codeList);
	}

}
