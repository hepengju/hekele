package com.hepengju.hekele.data.generator;

import java.util.Date;

/**
 * 字符生成器
 * 
 * <br> 默认生成0-64位的随机英文字符或数字
 * 
 * @author hepengju
 *
 */
public interface DateGenerator extends Generator<Date>{
	@Override
    Date generate();
}
