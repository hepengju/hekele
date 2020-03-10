package com.hepengju.hekele.base.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 比较两个JavaBean对象
 *
 * <pre>
 *     1. 仅比较简单属性, 即String, Integer, Date等, 参见: org.springframework.beans.BeanUtils#isSimpleValueType(java.lang.Class)
 *     2. 字段的中文描述, 采用Swagger的@ApiModelProperty
 * </pre>
 *
 * @author hepengju
 */
public class DiffUtil {

    public static List<DiffInfo> compare(Object oldObj, Object newObj, String... ignoreFields) {
        List<DiffInfo> diffList = new ArrayList<>();


        return null;
    }

    @ApiModel("差异信息")
    static class DiffInfo {
        private String name;       // 字段名称
        private String desc;       // 字段描述
        private String type;       // 差异类型: A-新增, U-修改, D-删除
        private String oldValue;   // 旧值
        private String newValue;   // 新值
    }

    @ApiModel("字段信息")
    @Data @AllArgsConstructor
    static class FieldInfo{
        private String name;
        private String desc;
        private Object value;
    }

    public static List<FieldInfo> getFieldInfoList(Object obj){
        List<FieldInfo> fiList = new ArrayList<>();
        if(obj == null) return fiList;

        // 所有的字段
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 去掉static, final, transient修饰的属性
            if(Modifier.isStatic(field.getModifiers())) continue;
            if(Modifier.isFinal(field.getModifiers())) continue;
            if(Modifier.isTransient(field.getModifiers())) continue;

            String name = field.getName();
            String desc = null;
            ApiModelProperty amp = field.getAnnotation(ApiModelProperty.class);
            if(amp != null) desc = amp.value();

            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                fiList.add(new FieldInfo(name, desc, value));
            } catch (IllegalAccessException e) {
            }
        }

        return fiList;
    }
}
