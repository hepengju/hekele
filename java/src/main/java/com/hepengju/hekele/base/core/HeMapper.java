package com.hepengju.hekele.base.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 基础 Mapper类
 */
public interface HeMapper<T> extends BaseMapper<T> {

    // 扩展MybatisPlus的方法: 启用,禁用
    int enableById(Serializable id);
    int enableBatchByIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);
    int disableById(Serializable id);
    int disableBatchByIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);

    // 新增: 带创建时间和用户
    default Integer addWithCreate(T entity) {
        try {BeanUtils.setProperty(entity, "createTime"    , new Date());    } catch (Exception e) {}
        try {BeanUtils.setProperty(entity, "createUserCode", Now.userCode());} catch (Exception e) {}
        try {BeanUtils.setProperty(entity, "createUserName", Now.userName());} catch (Exception e) {}
        return insert(entity);
    }

   // 编辑: 带更新时间和用户
    default Integer editWithUpdate(T entity) {
        try {BeanUtils.setProperty(entity, "updateTime"    , new Date())    ;} catch (Exception e) {}
        try {BeanUtils.setProperty(entity, "updateUserCode", Now.userCode());} catch (Exception e) {}
        try {BeanUtils.setProperty(entity, "updateUserName", Now.userName());} catch (Exception e) {}
        return updateById(entity);
    }
}
