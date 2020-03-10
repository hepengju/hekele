package com.hepengju.hekele.base.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Field;

/**
 * BO类的工具类
 */
public class BOUtil {

    public static <T> String getApiModelValue(Class<T> clazz) {
        ApiModel apiModel = clazz.getAnnotation(ApiModel.class);
        if (apiModel != null) return apiModel.value();
        return "";
    }

    public static <T> String getApiModelPropertyValue(Class<T> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            return apiModelProperty.value();
        } catch (Exception e) {
            return "";
        }
    }

}
