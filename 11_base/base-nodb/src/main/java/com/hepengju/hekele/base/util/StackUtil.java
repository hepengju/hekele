package com.hepengju.hekele.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 堆栈工具
 * 
 * @author he_pe 2018-03-05
 *
 */
public class StackUtil {
	
	/**
	 * 取得e.printStackTrace()到字符串用于保存
	 */
	public static String getStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}
}
