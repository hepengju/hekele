package com.hepengju.hekele.base.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * SQL语句处理: 分割SQL语句为List集合(内含去除注释功能)
 * 
 * @author he_pe 2018-02-23
 *
 */
public class SqlUtil {
	
	// (?i)表示忽略大小写   \s表示空白字符  
	private static final String REG_YYYY = "(?i)\\{\\{\\s*yyyy\\s*\\}\\}";
	private static final String REG_YYYYMM = "(?i)\\{\\{\\s*yyyymm\\s*\\}\\}";
	private static final String REG_YYYYMMDD = "(?i)\\{\\{\\s*yyyymmdd\\s*\\}\\}";
	
	//行分隔符正则
	private static Pattern lineBreakPattern = Pattern.compile("(\\r?\\n)+");
	
	/**
	 * 替换SQL语句中的日期变量为当前实际值
	 * 
	 * <pre>
	 *  {{yyyy}}     --> 当前年份     '2018'
	 *  {{yyyymm}}   --> 当前年月     '201805'
	 *  {{yyyymmdd}} --> 当前年月日   '20180504'
	 * </pre>
	 */
	public static String replaceDateReg(String sql) {
		LocalDate localDate = LocalDate.now();
		return sql.replaceAll(REG_YYYY, "'" + DateTimeFormatter.ofPattern("yyyy").format(localDate) + "'")
				  .replaceAll(REG_YYYYMM, "'" + DateTimeFormatter.ofPattern("yyyyMM").format(localDate) + "'")
				  .replaceAll(REG_YYYYMMDD, "'" + DateTimeFormatter.ofPattern("yyyyMMdd").format(localDate) + "'");
	}

	/**
	 * 替换SQL语句中的日期变量为指定值
	 */
	public static String replaceDateReg(String sql,String yyyymmdd) {
		String newsql = "";
		String quoteYmd = "'" + yyyymmdd + "'";
		switch (yyyymmdd.length()) {
			case 4:
				newsql = sql.replaceAll(REG_YYYY, quoteYmd);
				break;
			case 6:
				newsql = sql.replaceAll(REG_YYYYMM, quoteYmd);
				break;
			case 8:
				newsql = sql.replaceAll(REG_YYYYMMDD, quoteYmd);
				break;
			default:
				newsql = sql;
		}
		return newsql;
	}
	
	
	/**
	 * 分割SQL语句为List集合: 默认分隔符为分号,转义符为单引号
	 */
	public static List<String> splitSQL(String sqlContent){
		String removeComment = removeComment(sqlContent);
		return splitSQL(removeComment, ';', '\'');
	}

	/**
	 * 去除注释: 默认注释为常规的SQL注释,转义符为单引号
	 */
	public static String removeComment(String sql) {
		return removeComment(sql, "--", "/*", "*/", '\'');
	}
	
	/**
	 * 去除注释,并转换为单行的SQL
	 * 
	 * <br> 应用: 数据规则的SQL语句在与其他平台交流时, 一行一个语句, 便于解析
	 */
	public static String removeCommentAndSingleLine(String sql) {
		String removeComment = removeComment(sql);
		//String singleLine = P6Util.singleLine(removeComment);
		String singleLine = lineBreakPattern.matcher(removeComment).replaceAll(" ");
		return singleLine;
	}
	
	/**
	 * 去除注释: 更通用的方法
	 * 
	 * @param fileContent 文件内容(字符串)
	 * @param lineComment 单行注释
	 * @param blockCommentBegin 多行注释开始
	 * @param blockCommentEnd 多行注释结束
	 * @param quote 引号
	 * @return 去除注释的字符串
	 */
	private static String removeComment(String fileContent, String lineComment, String blockCommentBegin,
			String blockCommentEnd, Character quote) {
		
		String tempStr = null;               //临时字符串
		boolean quoteFlag = false;           //引号标记
		boolean lineCommentFlag = false;     //单行注释标记
		boolean blockCommentFlag = false;    //多行注释标记
		
		StringBuilder sbuilder = new StringBuilder();
		char[] fchars = fileContent.toCharArray();
		
		//public String(char[] value,int offset,int count)
		//会抛出IndexOutOfBoundsException - 如果 offset 和 count 参数索引字符超出 value 数组的范围。
		//因此加入了m变量,在生成tempStr的时候进行预判断
		for (int i = 0,m = fchars.length; i < fchars.length; i++,m--) {
			
			//1.没有注释的情况下,遇到了引号,则反转引号标记
			if(quote != null && !lineCommentFlag && !blockCommentFlag && fchars[i] == quote){
				quoteFlag = !quoteFlag;
			}
			
			//2.没有引号标记的情况下,判断是否是注释
			if(!quoteFlag){
				//2.1单行注释的处理
				if(lineComment != null){
					//2.1.1单行注释开始
					tempStr = m > lineComment.length() ? new String(fchars,i,lineComment.length()) : "";
					if( !blockCommentFlag && lineComment.equalsIgnoreCase(tempStr)){
						lineCommentFlag = true;
					}
					//2.1.2单行注释结束,结束符保留
					if(lineCommentFlag && (fchars[i] == '\n' || fchars[i] == '\r' && fchars[i+1] == '\n')){
						lineCommentFlag = false;
					}
				}
				
				//2.2多行注释处理
				if(blockCommentBegin != null){
					//2.2.1多行注释开始
					tempStr = m > blockCommentBegin.length() ? new String(fchars,i,blockCommentBegin.length()) : "";
					if(!lineCommentFlag && blockCommentBegin.equalsIgnoreCase(tempStr)){
						blockCommentFlag = true;
					}
				}
				
				if(blockCommentEnd !=null){
					//2.2.2多行注释结束,结束符删除
					tempStr = m > blockCommentEnd.length() ? new String(fchars,i,blockCommentEnd.length()) : "";
					if( blockCommentFlag && blockCommentEnd.equalsIgnoreCase(tempStr)){
						blockCommentFlag = false;
						i = i + blockCommentEnd.length();
						m = m - blockCommentEnd.length();
						continue;
					}
				}
				
			}
			
			//如果不是注释,那么追加字符
			if(!lineCommentFlag && !blockCommentFlag) {
				sbuilder.append(fchars[i]);
			}
				
		}
		return sbuilder.toString();
	}

	/**
	 * 分割SQL语句为List集合: 更通用的方法
	 */
	private static List<String> splitSQL(String sql, Character split,Character quote) {
		
		List<String> sqlList = new ArrayList<String>();
		
		//如果sql语句为空返回大小为零的List
		if(sql == null) return sqlList;
		
		//如果分割字符为NULL,直接返回
		if(split == null){
			sqlList.add(sql.trim());
			return sqlList;
		}
		
		//引号标记
		boolean quoteFlag = false;           
		StringBuilder sbuilder = new StringBuilder();
		String temp = null;
		
		for (char c : sql.toCharArray()) {
			//1.遇到了引号,则反转引号标记
			if(quote != null && c == quote){
				quoteFlag = !quoteFlag;
			}
			
			//2.没有引号的情况下,遇到分割符则trim后追加
			if(!quoteFlag && c == split){
				temp = sbuilder.toString().trim();
				if(!"".equals(temp))
					sqlList.add(temp);
				sbuilder = new StringBuilder();
				continue;
			}
			sbuilder.append(c);
		}
		
		temp = sbuilder.toString().trim();
		if(!"".equals(temp))
			sqlList.add(temp);
		
		return sqlList;
	}	
}
