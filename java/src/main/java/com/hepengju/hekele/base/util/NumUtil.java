package com.hepengju.hekele.base.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 数字工具
 * 
 * <br> 优先使用Apache-Lang组件的 @NumberUtils, 对于没有的功能在此处追加
 * @author he_pe 2018-03-05
 *
 */
public class NumUtil {

	//默认分隔符
	public static final String DEFAULT_SPLIT = ",";
	
	/**
	 * 根据分隔符将字符串分隔为数组,并转为List<Integer>集合
	 * 
	 * <pre>
	 * 	示例: "1,2,3,4" --> List<Integer>
	 * </pre>
	 */
	public static List<Integer> toList(String ids) {
		if(StringUtils.isBlank(ids)) 
			return new ArrayList<Integer>();
		return Arrays.asList(ids.split(DEFAULT_SPLIT))
				.stream().filter(Objects::nonNull)
				.map(Integer::parseInt).collect(Collectors.toList());
	}
	
	/**
	 * 字符串ids中移除id
	 */
	public static String removeId(String ids,Integer id) {
		return toList(ids).stream().filter(i -> !i.equals(id)).map(String::valueOf).collect(Collectors.joining(DEFAULT_SPLIT));
	}	
	
	/**
	 * 字符串ids中添加id
	 */
	public static String addId(String ids,Integer id) {
		if(StringUtils.isBlank(ids)) {
			return String.valueOf(id);
		}else {
			return ids + DEFAULT_SPLIT + id;
		}
	}
}
