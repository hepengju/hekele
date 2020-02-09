package com.hepengju.hekele.data.generator.string;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data @ApiModel("随机中文字生成器")
public class RandomNameGenerator implements StringGenerator {
	
	private int min = 2;
	private int max = 4;

	@Override
	public String generate() {
		return RandomUtil.randomNum(DataConst.words, min, max);
	}

}
