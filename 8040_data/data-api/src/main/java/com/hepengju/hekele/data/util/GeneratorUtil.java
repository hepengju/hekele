package com.hepengju.hekele.data.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hepengju.hekele.base.core.Now;
import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.base.util.ExcelUtil;
import com.hepengju.hekele.base.util.PrintUtil;
import com.hepengju.hekele.base.util.StringUtil;
import com.hepengju.hekele.base.util.WebUtil;
import com.hepengju.hekele.data.annotation.GeneratorAnno;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.generator.gen300_string.NullGenerator;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
    public static List<String> getColumnTitleList(Class<?> boCLass) {
        return getColumnList(boCLass, true);
    }

    public static List<String> getColumnNameList(Class<?> boCLass) {
        return getColumnList(boCLass, false);
    }

    /**
     * 获取列名称列表
     */
    public static List<String> getColumnList(Class<?> boCLass, boolean isTitle) {
        List<String> columnNameList = new ArrayList<>();
        Field[] fields = boCLass.getDeclaredFields();
        for (Field field : fields) {
            String columnName = StringUtil.camelToUnderline(field.getName());
            if (!isTitle) columnNameList.add(columnName.toUpperCase());;

            TableField tableField = field.getAnnotation(TableField.class);
            if(tableField != null && StringUtils.isNotBlank(tableField.value())) columnName = tableField.value();
            columnNameList.add(columnName.toUpperCase());
        }
        return columnNameList;
    }

    /**
     * 根据实体类生成批量数据
     */
    public static List<? extends List<? extends Object>> getDataList(Class<?> boClass, int count) {
        List<Generator> generatorList = getGeneratorList(boClass);
        return getDataList(generatorList, count);
    }

    /**
     * 根据生成器生成批量数据
     */
    public static List<? extends List<? extends Object>> getDataList(List<Generator> genList, int count) {
        List<List<? extends Object>> dataList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            List<Object> rowList = new ArrayList<>(genList.size());
            for (Generator generator : genList) {
                rowList.add(generator.generate());
            }
            dataList.add(rowList);
        }
        return dataList;
    }

    public static List<List<String>> getDataStringList(Class<?> boClass, int count) {
        List<Generator> generatorList = getGeneratorList(boClass);
        return getDataStringList(generatorList, count);
    }

    public static List<List<String>> getDataStringList(List<Generator> genList, int count) {
        List<List<String>> dataList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            List<String> rowList = new ArrayList<>(genList.size());
            for (Generator generator : genList) {
                rowList.add(generator.generateString());
            }
            dataList.add(rowList);
        }
        return dataList;
    }

    /**
     * 根据实体类获得生成器列表
     */
    public static List<Generator> getGeneratorList(Class<?> boClass) {
        List<Generator> genList = new ArrayList<>();

        Field[] fields = boClass.getDeclaredFields();
        for (Field field : fields) {
            GeneratorAnno gn = field.getAnnotation(GeneratorAnno.class);
            if (gn != null) {
                try {
                    Generator gen = gn.value().newInstance();
                    setNotBlankProperty(gen, "min", gn.min());
                    setNotBlankProperty(gen, "max", gn.max());
                    setNotBlankProperty(gen, "code", gn.code());
                    setNotBlankProperty(gen, "codeMulti", gn.codeMulti());
                    setNotBlankProperty(gen, "format", gn.format());
                    setNotBlankProperty(gen, "prefix", gn.prefix());
                    setNotBlankProperty(gen, "suffix", gn.suffix());
                    genList.add(gen);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                }
            } else {
                genList.add(new NullGenerator());
            }

        }
        return genList;
    }

    private static void setNotBlankProperty(Generator generator, String propertyName, Object propertyValue) throws InvocationTargetException, IllegalAccessException {
        if (propertyValue instanceof String) {
            if (StringUtils.isNotBlank((String) propertyValue)) {
                BeanUtils.setProperty(generator, propertyName, propertyValue);
            }
        } else {
            if (propertyValue != null) {
                BeanUtils.setProperty(generator, propertyName, propertyValue);
            }
        }
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * 下载测试数据
     */
    public static void downloadDataList(Class<?> clazz, int count, String fileFormat) {
        // Excel下载时
        //List<? extends List<? extends Object>> dataList = "excel".equals(dataFormat)
        //        ? GeneratorUtil.getDataList(clazz, count)
        //        : GeneratorUtil.getDataStringList(clazz, count);
        String tableName = GeneratorUtil.getTableName(clazz);
        handleDataList(getDataStringList(clazz, count)
                , fileFormat
                , tableName, tableName
                , getColumnTitleList(clazz), getColumnNameList(clazz));
    }

    /**
     * 处理下载数据
     */
    @SneakyThrows
    public static void handleDataList(List<? extends List<? extends Object>> dataList
            , String fileFormat
            , String fileName, String tableName
            , List<String> columnTitleList, List<String> columnNameList){

        fileName = fileName + "-" + Now.yyyyMMddHHmmss();
        if ("sql".equals(fileFormat)) {
            String result = PrintUtil.printInsert(tableName, columnNameList, dataList, true);
            WebUtil.handleFileDownload(fileName + ".sql", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("csv".equals(fileFormat)) {
            String result = PrintUtil.printCSV(dataList);
            WebUtil.handleFileDownload(fileName + ".csv", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("tsv".equals(fileFormat)) {
            String result = PrintUtil.printTSV(dataList);
            WebUtil.handleFileDownload(fileName + ".tsv", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("excel".equals(fileFormat)) {
            WebUtil.handleFileDownload(fileName + ".xlsx");
            ExcelUtil.exportFromList(columnTitleList, dataList, WebUtil.getHttpServletResponse().getOutputStream());
            return;
        }

        throw new HeException("格式暂不支持");
    }
}
