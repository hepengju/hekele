package com.hepengju.hekele.base.core.sqlhandle;

import java.util.List;

/**
 * Select语句的结果
 * 
 * @author he_pe 2018-04-22
 *
 */
public class SelectResult {

	private List<String>       titleList;  // 标题区
	private List<List<Object>> recordList; // 数据区

	public List<String> getTitleList() {
		return titleList;
	}

	public void setTitleList(List<String> titleList) {
		this.titleList = titleList;
	}

	public List<List<Object>> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<List<Object>> recordList) {
		this.recordList = recordList;
	}

	@Override
	public String toString() {
		return "SelectResult [titleList=" + titleList + ", recordList=" + recordList + "]";
	}

}