package com.hepengju.hekele.base.util;

import com.p6spy.engine.common.P6Util;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 字符串工具
 * 
 * <ol>
 * <li> 驼峰-下划线 camelToUnderline, underlineToCamel  {@link com.baomidou.mybatisplus.toolkit.StringUtils}
 * <li> 全角-半角   toSBC, toDBC
 *          <ol>
 * 			<li> 1.半角字符是从33开始到126结束
 * 			<li> 2.与半角字符对应的全角字符是从65281开始到65374结束
 * 			<li> 3.其中半角的空格是32.对应的全角空格是12288
 *          </ol>
 * <li> 简单分割(不用正则)   split
 * <li> 单行-多行   singleLine,singleLineAddN, multiLineRmN
 *          singleLineAddN, multiLineRmN 在进行数据库二维表的文件交换时使用
 * </ol>
 * 
 * 
 * @author hepengju
 *
 */
public class StringUtil {

    /**
     * Spring的变量替换功能提取: ${varName:defaultName}
     */
    private static PropertyPlaceholderHelper helperDollar = new PropertyPlaceholderHelper("${", "}",":",true);
    private static PropertyPlaceholderHelper helperStar   = new PropertyPlaceholderHelper("*{", "}",":",true);
    private static PropertyPlaceholderHelper helperPound  = new PropertyPlaceholderHelper("#{", "}",":",true);
    private static PropertyPlaceholderHelper helperAt     = new PropertyPlaceholderHelper("@{", "}",":",true);
    
    /**
     * 替换变量: 支持 ${}, #{}, *{}, @{}; 支持默认值
     */
    public static String replaceVar(String str, Properties prop) {
    	if(prop == null) return str;
    	
        prop = toStringProperties(prop);
        String one = helperDollar.replacePlaceholders(str, prop);
        String two = helperStar.replacePlaceholders(one, prop);
        String three = helperPound.replacePlaceholders(two, prop);
        String four = helperAt.replacePlaceholders(three, prop);
        return four;
    }

    /**
     * Spring的解析器, 需要Value都是String类型
     */
    private static Properties toStringProperties(Properties prop) {
        Properties newProp = new Properties();
        prop.forEach((k,v) -> newProp.put(k, String.valueOf(v)));
        return newProp;
    }

	/**
	 * trim处理
	 */
	public static String trim(String str){
    	return str == null ? null : str.trim();
	}

	/**
	 * 是否空白
	 * 
	 * @see org.apache.commons.lang3.StringUtils#isBlank(CharSequence)
	 */
    public static boolean isBlank(final CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
	/**
	 * 字符串驼峰转下划线格式
	 */
	public static String camelToUnderline(String param) {
		return camelToUnderline(param,'_');
	}

	/**
	 * 字符串下划线转驼峰格式
	 */
	public static String underlineToCamel(String param) {
		return underlineToCamel(param,'_');
	}
	
	/**
	 * 字符串驼峰转下划线格式更通用(比如转中划线)
	 */
	public static String camelToUnderline(String param, char underline) {
		if (isBlank(param)) return "";
		
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (Character.isUpperCase(c) && i > 0) {
				sb.append(underline);
			}
			sb.append(Character.toLowerCase(c));
		}
		return sb.toString();
	}

	/**
	 * 字符串下划线转驼峰格式(比如中划线转)
	 */
	public static String underlineToCamel(String param, char underline) {
		if (isBlank(param)) return "";

		String temp = param.toLowerCase();
		int len = temp.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = temp.charAt(i);
			if (c == underline) {
				if (++i < len) {
					sb.append(Character.toUpperCase(temp.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 转换为全角字符
	 */
	public static char toDBC(char c) {
		if(c == 32) {
			return (char) 12288;
		}else if (c >=33 && c <=126){
			return (char) (c + 65248) ;
		}else {
			return c;
		}
	}
	
	/**
	 * 转换为半角字符
	 */
	public static char toSBC(char c) {
		if(c == 12288) {
			return (char) 32;
		}else if (c >=65281 && c <=65374){
			return (char) (c - 65248) ;
		}else {
			return c;
		}
	}
	
	/**
	 * 转换为全角字符串
	 */
	public static String toDBC(String str) {
		return toDBC(str, false);
	}
	
	/**
	 * 转换为半角字符串
	 */
	public static String toSBC(String str) {
		return toSBC(str, false);
	}
	
	/**
	 * 转换为全角字符串
	 * 
	 * @param expectSingleQuote 是否排除单引号内部的字符
	 */
	public static String toDBC(String str, boolean expectSingleQuote) {
		if(str == null || str.length() == 0) return str;
		char[] charArray = str.toCharArray();
		StringBuilder sb = new StringBuilder(charArray.length);
		if(!expectSingleQuote) {
			for (char c : charArray) {
				sb.append(toDBC(c));
			}
		}else {
			boolean isQuote = false;
			for (char c : charArray) {
				if(c == '\'') isQuote = !isQuote;  //遇到引号反转
				sb.append(isQuote ? c : toDBC(c));
			}
		}
		
		return sb.toString();
	}	
	
	/**
	 * 转换为半角字符串
	 * @param expectSingleQuote 是否排除单引号内部的字符
	 */
	public static String toSBC(String str, boolean expectSingleQuote) {
		if(str == null || str.length() == 0) return str;
		char[] charArray = str.toCharArray();
		StringBuilder sb = new StringBuilder(charArray.length);
		if(!expectSingleQuote) {
			for (char c : charArray) {
				sb.append(toSBC(c));
			}
		}else {
			boolean isQuote = false;
			for (char c : charArray) {
				if(c == '\'') isQuote = !isQuote;  //遇到引号反转
				sb.append(isQuote ? c : toSBC(c));
			}
		}
		return sb.toString();
	}	
	
	/**
	 * 简单分割
	 * 
	 * @see freemarker.template.utility.StringUtil#split(String, String, boolean)
	 */
	public static String[] split(String str, String sep) {

        int i, b, e;
        int cnt;
        String res[];
        int ln = str.length();
        int sln = sep.length();

        if (sln == 0) throw new IllegalArgumentException("The separator string has 0 length");

        i = 0;
        cnt = 1;
        while ((i = str.indexOf(sep, i)) != -1) {
            cnt++;
            i += sln;
        }
        res = new String[cnt];

        i = 0;
        b = 0;
        while (b <= ln) {
            e = str.indexOf(sep, b);
            if (e == -1) e = ln;
            res[i++] = str.substring(b, e);
            b = e + sln;
        }
        return res;
	}
	
	private static Pattern lineBreakPattern = Pattern.compile("(\\r?\\n)+");
	
	/**
	 * 转换为单行
	 * 
	 * @see P6Util#singleLine(String)
	 */
	public static String singleLine(String str) {
		return str == null ? str : lineBreakPattern.matcher(str).replaceAll(" ");
	}
	
	
	/**
	 * 转换为单行, 添加"\n",以便再次解析出来
	 */
	public static String singleLineAddN(String str) {
		return str == null ? str : lineBreakPattern.matcher(str).replaceAll("\\\\n");
	}
	
	/**
	 * 将含有\n的单行文本转换为多行
	 */
	public static String multiLineRmN(String str) {
		return str == null ? str : str.replaceAll("\\\\n","\n");
	}
}
