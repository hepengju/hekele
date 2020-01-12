package com.hepengju.hekele.data.generator.string.name;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.StringGenerator;
import com.hepengju.hekele.data.generator.config.DataConst;

/**
 * 中文名
 * 
 * <br>
 * 常见汉字: 随机生成1~2个汉字的名字
 * 
 * @author hepengju
 *
 */
public class ChineseLastNameGenerator implements StringGenerator {

	private String[] words = DataConst.words;

	@Override
	public String generate() {
		return RandomUtil.randomNum(words, 1, 3);
	}

}
