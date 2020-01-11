package com.hepengju.hekele.base.core.excel;

import com.hepengju.hekele.base.core.exception.ExcelCheckException;
import com.hepengju.hekele.base.util.ExcelUtil;

import java.util.List;
import java.util.Map;

/**
 * Excel工作表的校验接口
 * 
 * <p> 将相关信息都给出, 可以编写实现类进行校验, 不满足校验抛出运行时异常OssException
 * 
 * @author hepengju
 *
 */
public interface ExcelSheetCheck {
    
    /**
     * @param sheetName     工作表名称
     * @param columnCount   工作表配置读取的列的个数
     * @param dataBeginRow  配置的数据开始行, 默认为2
     * @param titleIndexMap key->标题,val->索引  20190731 修正接口方法，将标题与列索引对应起来当参数使用
     * @param dataList      数据List<List>
     * @return              错误信息List
     */
    List<String> checkResult(String sheetName, Integer columnCount, Integer dataBeginRow, Map<String, Integer> titleIndexMap, List<List<Object>> dataList) throws ExcelCheckException;

    /**
     * 英文的列名转换为List的下标索引, 从0开始, 便于用易读的方式获取索引
     */
    default int enToInx(String colEn){
        return ExcelUtil.getColumnIndexByEnName(colEn) - 1;
    }

}
