package com.hepengju.hekele.base.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * BeanUtil
 *
 * @author hepengju
 */
public class BeanUtil {

    /**
     * 获取值: 获取不到返回null
     */
    public static String getProperty(final Object bean, final String name) {
        try {
            return org.apache.commons.beanutils.BeanUtils.getProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }

    /**
     * 设置非空属性
     */
    private static void setNotBlankProperty(Object obj, String propertyName, Object propertyValue) {
        try {
            if (propertyValue instanceof String) {
                if (StringUtils.isNotBlank((String) propertyValue)) {
                    org.apache.commons.beanutils.BeanUtils.setProperty(obj, propertyName, propertyValue);
                }
            } else {
                if (propertyValue != null) {
                    org.apache.commons.beanutils.BeanUtils.setProperty(obj, propertyName, propertyValue);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
        }
    }

    /**
     * 复制非blank属性: 注意, 此处字段的类型不一致时, 复制附上
     */
    public static void copyNotBlankProperties(Object source, Object target) throws BeansException {
        //BeanUtils.copyProperties(source, target, getBlankPropertyNames(source));
        String[] notBlankPropertyNames = getNotBlankPropertyNames(source);
        for (String notBlankPropertyName : notBlankPropertyNames) {
            try {
                setNotBlankProperty(target, notBlankPropertyName, BeanUtils.getSimpleProperty(source, notBlankPropertyName));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            }
        }
    }

    /**
     * 获取blank属性名
     */
    public static String[] getNotBlankPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> notBlankNameSet = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue instanceof java.lang.String) {
                if (StringUtils.isBlank((java.lang.String) srcValue)) continue;
            } else {
                if (srcValue == null) continue;
            }
            notBlankNameSet.add(pd.getName());
        }
        String[] result = new String[notBlankNameSet.size()];
        return notBlankNameSet.toArray(result);
    }
}
