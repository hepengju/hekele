package com.hepengju.hekele.base.core.sqlhandle;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * SQL语句的执行情况
 * 
 * @author he_pe 2018-04-22
 */
@Data
public class SqlResult {

	private String  sqlContent;        // SQL语句内容
	private String  sqlType;           // Select,Update,Delete,Insert,Other
	private Integer limit;             // 限制行数
	private SelectResult SelectResult; // SELECT语句的执行结果
	private Integer affectedRows;      // 影响行数
	private Date beginTime;            // 开始时间
	private Date endTime;              // 结束时间
	private Long costMillis;           // 耗时(毫秒)

	@Data
	public static class SelectResult {
		private List<String> titleList;  // 标题区
		private List<List<Object>> recordList; // 数据区
	}
}
