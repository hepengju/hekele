package com.hepengju.hekele.base.core.sqlhandle;

import java.util.Date;

/**
 * SQL语句的执行情况
 * 
 * @author he_pe 2018-04-22
 */
public class SqlResult {

	private String sqlContent;         // SQL语句内容
	private String sqlType;            // Select,Update,Delete,Insert,Other
	private Integer limit;             // 限制行数
	private SelectResult SelectResult; // SELECT语句的执行结果
	private Integer affectedRows;      // 影响行数
	private Date beginTime;            // 开始时间
	private Date endTime;              // 结束时间
	private Long costMillis;           // 耗时(毫秒)

	public String getSqlContent() {
		return sqlContent;
	}

	public void setSqlContent(String sqlContent) {
		this.sqlContent = sqlContent;
	}

	public String getSqlType() {
		return sqlType;
	}

	public void setSqlType(String sqlType) {
		this.sqlType = sqlType;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public SelectResult getSelectResult() {
		return SelectResult;
	}

	public void setSelectResult(SelectResult selectResult) {
		SelectResult = selectResult;
	}

	public Integer getAffectedRows() {
		return affectedRows;
	}

	public void setAffectedRows(Integer affectedRows) {
		this.affectedRows = affectedRows;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getCostMillis() {
		return costMillis;
	}

	public void setCostMillis(Long costMillis) {
		this.costMillis = costMillis;
	}

	@Override
	public String toString() {
		return "SqlResult [sqlContent=" + sqlContent + ", sqlType=" + sqlType + ", limit=" + limit + ", SelectResult="
				+ SelectResult + ", affectedRows=" + affectedRows + ", beginTime=" + beginTime + ", endTime=" + endTime
				+ ", costMillis=" + costMillis + "]";
	}
	
}
