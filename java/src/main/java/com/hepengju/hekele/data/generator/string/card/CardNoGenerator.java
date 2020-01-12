package com.hepengju.hekele.data.generator.string.card;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.config.DataConst;
import lombok.Data;

/**
 * 卡号生成器
 * 
 * @author hepengju
 *
 */
@Data
public class CardNoGenerator implements StringGenerator {

	private int length = 16;
	
	@Override
	public String generate() {
		return RandomUtil.randomNum(DataConst.numbers, length);
	}

}
