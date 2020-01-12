package com.hepengju.hekele.data;

/**
 * 字符生成器
 * 
 * <br> 默认生成0-64位的随机英文字符或数字
 * 
 * @author hepengju
 *
 */
public interface StringGenerator extends Generator<String>{
	@Override
    String generate();
}
