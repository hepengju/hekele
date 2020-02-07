package com.hepengju.hekele.base.core.excel;

import lombok.Data;

import java.util.List;

/**
 * Excel工作薄
 *
 * @author he_pe
 */
@Data
public class ExcelBookConfig {

    private String beforeSqls;                   // 导入之前调用的SQL语句
    private String afterSqls;                    // 导入之后调用的SQL语句(存储过程)
    private List<ExcelSheetConfig> sheetList;    // 工作表集合

}
