package com.hepengju.hekele.data.generator.gen400_custom.gen410_name;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 中文名
 * 
 * <br>
 * 常见汉字: 随机生成1~2个汉字的名字
 * 
 * @author hepengju
 *
 */
@ApiModel("中文名生成器")@Data
public class ChineseLastNameGenerator extends AbstractStringGenerator {

	private String[] words = DataConst.words;

	@Override
	public String generate() {
		return RandomUtil.randomNum(words, 1, 3);
	}

}
