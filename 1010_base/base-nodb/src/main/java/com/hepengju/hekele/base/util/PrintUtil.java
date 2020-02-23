package com.hepengju.hekele.base.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集合打印工具类
 */
public class PrintUtil {

    public static final int BATCH_SIZE = 500;
    public static String printTSV(List<? extends List<? extends Object>> dataList){
        return printWithSeparator(dataList,"\t");
    }
    public static String printCSV(List<? extends List<? extends Object>> dataList){
        return printWithSeparator(dataList,",");
    }

    public static String printWithSeparator(List<? extends List<? extends Object>> dataList, String separator){
        StringBuilder sb = new StringBuilder();
        for (List<? extends Object> rowList : dataList) {
            String row = rowList.stream().map(PrintUtil::format).collect(Collectors.joining(separator));
            sb.append(row);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String printInsert(String tableName, List<String> columnNameList, List<? extends List<? extends Object>> dataList, boolean batchInsert) {
        String columnComma = columnNameList.stream().collect(Collectors.joining(", "));

        StringBuilder sb = new StringBuilder();

        // 批量插入
        if (batchInsert) {
            String preSql = "INSERT INTO " + tableName + " (" + columnComma + ") \n     VALUES ";
            sb.append(preSql);
            for (int i = 1; i <= dataList.size(); i++) {
                List<? extends Object> rowList = dataList.get(i - 1);
                String sufSql = rowList.stream().map(PrintUtil::formatForInsert).collect(Collectors.joining(", ", "(", ")"));
                sb.append(sufSql);
                if (i % BATCH_SIZE != 0 && i != dataList.size()) sb.append("\n          , ");
                if (i % BATCH_SIZE == 0 && i != dataList.size()) sb.append("\n  ;\nCOMMIT;\n-- 第").append(i).append("条数据完毕\n\n").append(preSql);
            }
            sb.append("\n  ;\nCOMMIT;");
        } else {
            // 正常插入
            String preSql = "INSERT INTO " + tableName + " (" + columnComma + ") VALUES ";
            for (List<? extends Object> rowList : dataList) {
                String sufSql = rowList.stream().map(PrintUtil::formatForInsert).collect(Collectors.joining(", ", "(", ")"));
                sb.append(preSql).append(sufSql).append(";\n");
            }
            sb.append("COMMIT;");
        }

        return sb.append("\n-- 所有数据(").append(dataList.size()).append("条)插入完毕").toString();
    }

    public static String format(Object obj) {
        if (obj == null) return "";
        if (obj instanceof Date)          return DateUtil.dateToString((Date) obj);
        if (obj instanceof LocalDate)     return DateUtil.dateToString((LocalDate) obj);
        if (obj instanceof LocalDateTime) return DateUtil.dateToString((LocalDateTime) obj);
        return obj.toString();
    }

    public static String formatForInsert(Object obj) {
        if (obj == null)           return null;
        if (obj instanceof Number) return obj.toString();
        if (obj instanceof Date)          return appendQuota(DateUtil.dateToString((Date) obj));
        if (obj instanceof LocalDate)     return appendQuota(DateUtil.dateToString((LocalDate) obj));
        if (obj instanceof LocalDateTime) return appendQuota(DateUtil.dateToString((LocalDateTime) obj));
        return appendQuota(obj.toString());
    }

    private static String appendQuota(String str) {
        return "'" + str + "'";
    }

}
