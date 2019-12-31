package com.hepengju.hekele.base.core.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.hepengju.hekele.base.constant.HeConst;
import com.hepengju.hekele.base.core.exception.HeException;
import lombok.Data;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的简化Excel处理的监听器
 *
 * @author he_pe 2019-12-25
 */
@Data
public class ExcelListener extends AnalysisEventListener<Object> {
    private static List<String> excelExtNameList = HeConst.EXCEL_EXT_NAME_LIST;

    private boolean onlySheetName   = false;  // 跳过行处理(仅仅读取Sheet名称时使用)
    private boolean onlyTitle       = false;  // 只需要标题
    private boolean onlyData        = false;  // 只需要数据
    
    private int currentRow    = 0;      // 当前行
    private int titleRow      = 1;      // 默认标题行为第一行
    private int dataBeginRow  = 2;      // 默认数据行为第二行开始
    private Integer lastColumnIndex;    // 最后列
    private List<List<String>> dataList = new ArrayList<>();   // 保存数据

    public ExcelListener() {}
    public ExcelListener(boolean onlySheetName, boolean onlyTitle, boolean onlyData) {
        super();
        this.onlySheetName = onlySheetName;
        this.onlyTitle = onlyTitle;
        this.onlyData = onlyData;
    }
    public ExcelListener(boolean onlySheetName, boolean onlyTitle, boolean onlyData, int titleRow, int dataBeginRow, Integer lastColumnIndex) {
        super();
        this.onlySheetName = onlySheetName;
        this.onlyTitle = onlyTitle;
        this.onlyData = onlyData;
        this.titleRow = titleRow;
        this.dataBeginRow = dataBeginRow;
        this.lastColumnIndex = lastColumnIndex;
    }


    public ExcelTypeEnum getExcelType(String fileName) {
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        if(!excelExtNameList.contains(extension)) throw new HeException("excel.suffixError");
        ExcelTypeEnum type = "xls".equals(extension) ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX;
        return type;
    }
    
    @Override
    public void invoke(Object object, AnalysisContext context) {
        currentRow ++;
        if(onlySheetName) return;                                                       // 跳过行处理(仅仅读取Sheet名称时使用)
        if(onlyTitle && currentRow == titleRow) { readDataNormal(object); return; }     // 只需要标题
        if(onlyData  && currentRow >= dataBeginRow) { readDataNormal(object); return; } // 只需要数据
        readDataNormal(object);                                                         // 正常读取数据情况
    }

    private void readDataNormal(Object object) {
        // 正常读取数据
        if (object != null ) {
            @SuppressWarnings("unchecked")
            List<String> srcList = (List<String>) object;
            
            if(this.lastColumnIndex == null || srcList.size() == this.lastColumnIndex) { // 没有指定最后列或长度正好相等
                dataList.add(srcList);
            }else {
                if(srcList.size() > this.lastColumnIndex) {                              // 读出来的多, 只要需要的
                    List<String> newList = new ArrayList<>(this.lastColumnIndex);
                    for (int i = 0; i < this.lastColumnIndex; i++) {
                        newList.add(srcList.get(i));
                    }
                    dataList.add(newList);
                }else { 
                    for (int i = srcList.size(); i < this.lastColumnIndex; i++) {        // 读出来的少, 补充null
                        srcList.add(null);
                    }
                    dataList.add(srcList);
                }
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {}

    // 重置: 更换一个Sheet后, 新建数据并重置当前行
    public void resetDataList() {
        this.dataList = new ArrayList<>();
        this.currentRow = 0;
    }
}