package com.hepengju.hekele.data.generator.string.card;


import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import lombok.Data;

/**
 * 卡号生成器
 * 
 * @author hepengju
 *
 */
@Data
public class CardNoGenerator implements StringGenerator {

	private int max = 16;
	
	@Override
	public String generate() {
		return RandomUtil.randomNum(DataConst.numbers, max);
	}

}