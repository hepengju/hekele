package com.hepengju.hekele.data.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hepengju.hekele.base.core.Now;
import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.base.util.ExcelUtil;
import com.hepengju.hekele.base.util.PrintUtil;
import com.hepengju.hekele.base.util.StringUtil;
import com.hepengju.hekele.base.util.WebUtil;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.generator.string.NullGenerator;
import com.hepengju.hekele.data.meta.MetaGenerator;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成器工具类
 */
public class GeneratorUtil {

    /**
     * 获取表名称
     */
    public static String getTableName(Class<?> boClass) {
        TableName tableName = boClass.getAnnotation(TableName.class);
        return tableName == null ? StringUtil.camelToUnderline(boClass.getSimpleName()).toUpperCase() : tableName.value().toUpperCase();
    }

    /**
     * 获取列名称列表
     */
    public static List<String> getColumnNameList(Class<?> boCLass) {
        List<String> columnNameList = new ArrayList<>();
        Field[] fields = boCLass.getDeclaredFields();
        for (Field field : fields) {
            String columnName = StringUtil.camelToUnderline(field.getName());
            TableField tableField = field.getAnnotation(TableField.class);
            if(tableField != null && StringUtils.isNotBlank(tableField.value())) columnName = tableField.value();
            columnNameList.add(columnName.toUpperCase());
        }
        return columnNameList;
    }

    /**
     * 根据生成器生成批量数据
     */
    public static List<List<Object>> getDataList(List<Generator> genList, int count) {
        List<List<Object>> dataList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            List<Object> rowList = new ArrayList<>(genList.size());
            for (Generator generator : genList) {
                rowList.add(generator.generate());
            }
            dataList.add(rowList);
        }
        return dataList;
    }

    /**
     * 根据实体类生成批量数据
     */
    public static List<List<Object>> getDataList(Class<?> boClass, int count) {
        List<Generator> generatorList = getGeneratorList(boClass);
        return getDataList(generatorList, count);
    }

    /**
     * 根据实体类获得生成器列表
     */
    public static List<Generator> getGeneratorList(Class<?> boClass) {
        List<Generator> genList = new ArrayList<>();

        Field[] fields = boClass.getDeclaredFields();
        for (Field field : fields) {
            com.hepengju.hekele.data.annotation.Generator gn = field.getAnnotation(com.hepengju.hekele.data.annotation.Generator.class);
            if (gn != null) {
                MetaGenerator metaGen = new MetaGenerator();
                if(StringUtils.isNotBlank(gn.min())) metaGen.setMin(gn.min());
                if(StringUtils.isNotBlank(gn.max())) metaGen.setMax(gn.max());
                if(StringUtils.isNotBlank(gn.code())) metaGen.setCode(gn.code());
                if(StringUtils.isNotBlank(gn.codeMulti())) metaGen.setCodeMulti(gn.codeMulti());
                metaGen.setScale(gn.scale());
                metaGen.setClassName(gn.value().getName());
                genList.add(metaGen.toGenerator());
            } else {
                genList.add(new NullGenerator());
            }

        }
        return genList;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * 下载测试数据
     */
    public static void downloadDataList(Class<?> clazz, int count, String dataFormat) {
        List<List<Object>> dataList = GeneratorUtil.getDataList(clazz, count);

        String tableName = GeneratorUtil.getTableName(clazz);
        List<String> columnNameList = GeneratorUtil.getColumnNameList(clazz);
        handleDataList(dataList, dataFormat, tableName, columnNameList);
    }

    /**
     * 处理下载数据
     */
    @SneakyThrows
    public static void handleDataList(List<List<Object>> dataList, String dataFormat, String tableName, List<String> columnNameList){
        String fileName = tableName + "-" + Now.yyyyMMddHHmmss();
        if ("sql".equals(dataFormat)) {
            String result = PrintUtil.printInsert(tableName, columnNameList, dataList, true);
            WebUtil.handleFileDownload(fileName + ".sql", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("csv".equals(dataFormat)) {
            String result = PrintUtil.printCSV(dataList);
            WebUtil.handleFileDownload(fileName + ".csv", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("tsv".equals(dataFormat)) {
            String result = PrintUtil.printTSV(dataList);
            WebUtil.handleFileDownload(fileName + ".tsv", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("excel".equals(dataFormat)) {
            WebUtil.handleFileDownload(fileName + ".xlsx");
            ExcelUtil.exportFromList(columnNameList, dataList, WebUtil.getHttpServletResponse().getOutputStream());
            return;
        }

        throw new HeException("格式暂不支持");
    }
}
