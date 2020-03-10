package com.hepengju.hekele.base.util;

import io.swagger.annotations.ApiModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 比较两个JavaBean对象
 *
 * <br> 仅仅比较简单属性, 仅仅比较字段, 字段描述采用Swagger的@ApiModelProperty
 *
 * @author hepengju
 */
public class DiffUtil {


    public static List<DiffInfo> compare(Object oldObj, Object newObj, String... ignoreFields) {
        List<DiffInfo> diffList = new ArrayList<>();



        return null;
    }

    @ApiModel("差异比对信息")
    static class DiffInfo {
        private String name;       // 字段名称
        private String desc;       // 字段描述
        private String type;       // 差异类型: add, update, delete
        private String oldValue;   // 旧的类型
        private String newValue;   // 新的类型
    }

    private static List<DiffInfo> getFieldInfoList(Object obj){
        List<DiffInfo> list = new ArrayList<>();
        if(obj == null) return list;

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {

        }

        return list;
    }
}
