package com.hepengju.hekele.base.core.excel;

import lombok.Data;

/**
 * Excel工作表配置
 * 
 * @author he_pe
 */
@Data
public class ExcelSheetConfig {

	private String  sheetName;          //工作表名称
	private String  columnTitle;        //工作表列标题的检查
	private Integer columnCount;        //工作表列数
	private String  insertSql;          //插入SQL语句
	private Integer dataBeginRow = 2;   //数据开始行
	private ExcelSheetCheck excelSheetCheck;   // Excel工作表的校验类
	
	private boolean sheetNameMustExist      = true; // 工作表必须存在检查
	private boolean sheetNameDataMustExist  = true; // 工作表数据必须大于0检查
	
	private String beforeSqls;  // 预处理语句
	private String afterSqls;   // 后处理语句

    // 20190702 Excel的列个数不配置的话, 可以采用标题按照逗号分隔的长度
    public Integer getColumnCount() {
        return columnCount == null ? columnTitle.split(",").length : columnCount;
    }

}
