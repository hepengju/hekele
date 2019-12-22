package com.hepengju.hekele.base.core.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.hepengju.hekele.base.core.exception.HeException;
import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelListener extends AnalysisEventListener<Object> {
    private boolean onlySheetName   = false;  // 跳过行处理(仅仅读取Sheet名称时使用)
    private boolean onlyTitle = false;  // 只需要标题
    private boolean onlyData  = false;  // 只需要数据
    
    private int currentRow    = 0;      // 当前行
    private int titleRow      = 1;      // 默认标题行为第一行
    private int dataBeginRow  = 2;      // 默认数据行为第二行开始
    private Integer lastColumnIndex;    // 最后列
    private List<List<String>> datas = new ArrayList<>();   // 保存数据

    
    public ExcelListener() {}
    public ExcelListener(boolean onlySheetName, boolean onlyTitle, boolean onlyData) {
        super();
        this.onlySheetName = onlySheetName;
        this.onlyTitle = onlyTitle;
        this.onlyData = onlyData;
    }
    public ExcelListener(boolean onlySheetName, boolean onlyTitle, boolean onlyData, int titleRow,
                         int dataBeginRow, Integer lastColumnIndex) {
        super();
        this.onlySheetName = onlySheetName;
        this.onlyTitle = onlyTitle;
        this.onlyData = onlyData;
        this.titleRow = titleRow;
        this.dataBeginRow = dataBeginRow;
        this.lastColumnIndex = lastColumnIndex;
    }

    private static List<String> excelEndList = Arrays.asList("xls", "xlsx", "xlsm");
    public ExcelTypeEnum getExcelType(String fileName) {
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();
        if(!excelEndList.contains(extension)) throw new HeException("excel.suffixError");
        ExcelTypeEnum type = "xls".equals(extension) ? ExcelTypeEnum.XLS : ExcelTypeEnum.XLSX;
        return type;
    }
    
    @Override
    public void invoke(Object object, AnalysisContext context) {
        currentRow ++;
        // 跳过行处理(仅仅读取Sheet名称时使用)
        if(onlySheetName) return; 
        
        // 只需要标题
        if(onlyTitle && currentRow == titleRow) {
            readDataNormal(object);
            return;
        }
        
        // 只需要数据
        if(onlyData) {
            if(currentRow >= dataBeginRow) {
                readDataNormal(object);
            }
            return;
        }
        
        // 正常读取数据情况
        readDataNormal(object);
    }

    private void readDataNormal(Object object) {
        // 正常读取数据
        if (object != null ) {
            @SuppressWarnings("unchecked")
            List<String> srcList = (List<String>) object;
            
            if(this.lastColumnIndex == null || srcList.size() == this.lastColumnIndex) { // 没有指定最后列或长度正好相等
                datas.add(srcList);
            }else {
                if(srcList.size() > this.lastColumnIndex) {                              // 读出来的多, 只要需要的
                    List<String> newlist = new ArrayList<>(this.lastColumnIndex);
                    for (int i = 0; i < this.lastColumnIndex; i++) {
                        newlist.add(srcList.get(i));
                    }
                    datas.add(newlist);
                }else { 
                    for (int i = srcList.size(); i < this.lastColumnIndex; i++) {        // 读出来的少, 补充null
                        srcList.add(null);
                    }
                    datas.add(srcList);
                }
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {}
    public List<List<String>> getDatas() {return datas;}
    // 重置: 更换一个Sheet后, 新建数据并重置当前行
    public void resetDatas() {
        this.datas = new ArrayList<>();
        this.currentRow = 0;   
    }

    public boolean isOnlySheetName() {
        return onlySheetName;
    }

    public void setOnlySheetName(boolean onlySheetName) {
        this.onlySheetName = onlySheetName;
    }

    public boolean isOnlyTitle() {
        return onlyTitle;
    }

    public void setOnlyTitle(boolean onlyTitle) {
        this.onlyTitle = onlyTitle;
    }

    public boolean isOnlyData() {
        return onlyData;
    }

    public void setOnlyData(boolean onlyData) {
        this.onlyData = onlyData;
    }

    public int getTitleRow() {
        return titleRow;
    }

    public void setTitleRow(int titleRow) {
        this.titleRow = titleRow;
    }

    public int getDataBeginRow() {
        return dataBeginRow;
    }

    public void setDataBeginRow(int dataBeginRow) {
        this.dataBeginRow = dataBeginRow;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public void setDatas(List<List<String>> datas) {
        this.datas = datas;
    }
    public Integer getLastColumnIndex() {
        return lastColumnIndex;
    }
    public void setLastColumnIndex(Integer lastColumnIndex) {
        this.lastColumnIndex = lastColumnIndex;
    }
    
}