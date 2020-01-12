package com.hepengju.hekele.data.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hepengju.hekele.base.util.StringUtil;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.generator.string.NullGenerator;
import com.hepengju.hekele.data.meta.MetaGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成器工具类
 */
public class GeneratorUtil {

    /**
     * 获取表名称
     */
    public static String getTableName(Class<?> clazz) {
        TableName tableName = clazz.getAnnotation(TableName.class);
        return tableName == null ? StringUtil.camelToUnderline(clazz.getSimpleName()).toUpperCase() : tableName.value().toUpperCase();
    }

    /**
     * 获取列名称列表
     */
    public static List<String> getColumnNameList(Class<?> clazz) {
        List<String> columnNameList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String columnName = StringUtil.camelToUnderline(field.getName());
            TableField tableField = field.getAnnotation(TableField.class);
            if(tableField != null && StringUtils.isNotBlank(tableField.value())) columnName = tableField.value();
            columnNameList.add(columnName.toUpperCase());
        }
        return columnNameList;
    }

    /**
     * 生成批量数据
     */
    public static List<List<Object>> getDataList(Class<?> clazz, int count) {
        List<Generator> generatorList = getGeneratorList(clazz);
        return getDataList(generatorList, count);
    }

    public static List<List<Object>> getDataList(List<Generator> genList, int count) {
        List<List<Object>> dataList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Object> rowList = new ArrayList<>();
            for (Generator generator : genList) {
                rowList.add(generator.generate());
            }
            dataList.add(rowList);
        }

        return dataList;
    }

    public static List<Generator> getGeneratorList(Class<?> clazz) {
        List<Generator> genList = new ArrayList<>();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            com.hepengju.hekele.data.annotation.Generator gn = field.getAnnotation(com.hepengju.hekele.data.annotation.Generator.class);
            if (gn != null) {
                MetaGenerator metaGen = new MetaGenerator();
                metaGen.setMin(gn.min());
                metaGen.setMax(gn.max());
                metaGen.setCode(gn.code());
                metaGen.setScale(gn.scale());
                genList.add(getGenerator(gn.value(), metaGen));
            } else {
                genList.add(new NullGenerator());
            }

        }
        return genList;
    }

    public static Generator getGenerator(Class<? extends Generator> clazz, MetaGenerator metaGenerator) {
        try {
            Generator generator = clazz.newInstance();
            BeanUtils.copyProperties(metaGenerator, generator);
            return generator;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
