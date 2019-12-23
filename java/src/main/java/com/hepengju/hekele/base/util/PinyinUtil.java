package com.hepengju.hekele.base.util;

import com.github.promeg.pinyinhelper.Pinyin;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;

/**
 * 汉字转拼音工具
 * 
 * 
 * @author he_pe
 *
 */
public class PinyinUtil {

	/**
	 * 汉字转换为拼音
	 */
	public static String toPinyin(String src) {
		//控制判断
		if(StringUtils.isBlank(src)) {
			return "#";
		}
		
		//去掉空格
		src = src.trim(); 
		
		StringBuilder sb = new StringBuilder();
		for (char c : src.toCharArray()) {
			//如果c为汉字，则返回大写拼音；如果c不是汉字，则返回String.valueOf(c)
			String pinyin = Pinyin.toPinyin(c);
			sb.append(pinyin);
		}
		
		String result = sb.toString();
		
		//如果不是得到的拼音不是字母开头,则以#号开头
		if(!result.matches("^[A-Za-z].*")) {
			result = "#" + result;
		}
		
		return result;
	}
	
	/**
	 * 根据拼音进行排序
	 */
	public static void sort(List<String> srcList){
		srcList.sort(Comparator.comparing(PinyinUtil::toPinyin).thenComparing(s -> s));
		//srcList.sort((s1,s2) -> {
		//	int pinyinCompare = toPinyin(s1).compareTo(toPinyin(s2));
		//	return pinyinCompare == 0 ? s1.compareTo(s2) : pinyinCompare;
		//});
		
	}
}
