package com.hepengju.hekele.base.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 集合打印工具类
 */
public class PrintUtil {

    public static String printTSV(List<List<Object>> dataList){
        return printCommon(dataList,"\t");
    }

    public static String printCSV(List<List<Object>> dataList){
        return printCommon(dataList,",");
    }

    public static String printCommon(List<List<Object>> dataList, String sep){
        StringBuilder sb = new StringBuilder();
        for (List<Object> rowList : dataList) {
            for (Object obj : rowList) {
                sb.append(toString(obj)).append(sep);
            }
           sb.append("\n");
        }

        return sb.toString();
    }

    public static String toString(Object obj) {
        if (obj == null) return "";

        if (obj instanceof Date) {
            return DateUtil.dateToString((Date) obj);
        }

        if (obj instanceof LocalDate) {
            return DateUtil.dateToString((LocalDate) obj);
        }

        if (obj instanceof LocalDateTime) {
            return DateUtil.dateToString((LocalDateTime) obj);
        }

        return obj.toString();
    }
}
