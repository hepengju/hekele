package com.hepengju.hekele.base.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 比较两个JavaBean对象
 *
 * <pre>
 *     1. 仅比较简单属性, 即String, Integer, Date等, 参见: org.springframework.beans.BeanUtils#isSimpleValueType(java.lang.Class)
 *     2. 仅支持普通的JavaBean和Map
 *     3. 字段的中文描述, 采用Swagger的@ApiModelProperty
 * </pre>
 *
 * @author hepengju
 */
public class DiffUtil {

    public static final DateTimeFormatter localDateFormatter     = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<DiffInfo> get(Object oldObj, Object newObj, String... ignoreFields) {
        Map<String, FieldInfo> oldMap = getFieldInfoList(oldObj).stream().collect(Collectors.toMap(FieldInfo::getName, f -> f));
        Map<String, FieldInfo> newMap = getFieldInfoList(newObj).stream().collect(Collectors.toMap(FieldInfo::getName, f -> f));

        Set<String> allNameSet = new HashSet<>();
        allNameSet.addAll(oldMap.keySet());
        allNameSet.addAll(newMap.keySet());
        allNameSet.removeAll(Arrays.asList(ignoreFields));

        List<DiffInfo> diffList = new ArrayList<>();
        allNameSet.forEach(name -> {
            FieldInfo oldField = oldMap.get(name);
            FieldInfo newField = newMap.get(name);

            if (oldField == null && newField != null) {  // 新增
                diffList.add(new DiffInfo(name, newField.getDesc(), "A", null, valueToString(newField.getValue())));
            } else if (oldField != null && newField == null) { // 删除
                diffList.add(new DiffInfo(name, oldField.getDesc(), "D", valueToString(oldField.getValue()), null));
            } else if (oldField != null && newField != null) { // 变更
                String oldValue = valueToString(oldField.getValue());
                String newValue = valueToString(newField.getValue());
                String desc = StringUtils.isNotBlank(oldField.getDesc()) ? oldField.getDesc() : newField.getDesc();
                if (!newValue.equals(oldValue)) {
                    diffList.add(new DiffInfo(name, desc, "U", oldValue, newValue));
                }
            }
        });

        return diffList;
    }

    @ApiModel("差异信息")
    @Data @AllArgsConstructor
    static class DiffInfo {
        private String name;       // 字段名称
        private String desc;       // 字段描述
        private String type;       // 差异类型: A-新增, U-修改, D-删除
        private String oldValue;   // 旧值
        private String newValue;   // 新值
    }

    @ApiModel("字段信息")
    @Data
    @AllArgsConstructor
    static class FieldInfo {
        private String name;
        private String desc;
        private Object value;
    }

    public static List<FieldInfo> getFieldInfoList(Object obj) {
        List<FieldInfo> fiList = new ArrayList<>();
        if (obj == null) return fiList;

        // Map的特殊处理
        if (obj instanceof Map) {
            ((Map) obj).forEach((k, v) -> {
                if (notBlankAndSimple(k) && notBlankAndSimple(v)) {
                    fiList.add(new FieldInfo(k.toString(), null, v));
                }
            });
        } else {
            // 普通的BO类
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                // 去掉static, final, transient修饰的属性; 去掉非简单属性
                if (Modifier.isStatic(field.getModifiers())) continue;
                if (Modifier.isFinal(field.getModifiers())) continue;
                if (Modifier.isTransient(field.getModifiers())) continue;
                if (!BeanUtils.isSimpleProperty(field.getType())) continue;

                String name = field.getName();
                String desc = null;
                ApiModelProperty amp = field.getAnnotation(ApiModelProperty.class);
                if (amp != null) desc = amp.value();

                try {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    // 值非空白, 且是简单类型
                    if (notBlankAndSimple(value)) {
                        fiList.add(new FieldInfo(name, desc, value));
                    }

                } catch (IllegalAccessException e) {
                }
            }
        }
        return fiList;
    }

    private static boolean notBlankAndSimple(Object value) {
        return value != null && !"".equals(value.toString().trim()) && BeanUtils.isSimpleProperty(value.getClass());
    }

    private static String valueToString(Object value) {
        if(value == null) return "";

        if(value instanceof Date) {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
        } else if (value instanceof LocalDate) {
            return localDateFormatter.format((LocalDate) value);
        } else if (value instanceof LocalDateTime) {
            return localDateTimeFormatter.format((LocalDateTime) value);
        }



        return value.toString();
    }
}
