package com.hepengju.hekele.base.core;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hepengju.hekele.base.core.exception.HeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;

/**
 * 基础 Service类
 */
public class HeService<M extends HeMapper<T>, T> extends ServiceImpl<M,T> {

    @Autowired protected HeMapper<T> heMapper;

    // 查询某列指定值的个数
    public long countByColumnValue(String columnName, String columnValue, String idValue){ return heMapper.countByColumnValue(columnName, columnValue, idValue); }
    public long countByTwoColumnValue(String columnName1, String columnValue1, String columnName2, String columnValue2, String idValue) {
        return heMapper.countByTwoColumnValue(columnName1, columnValue1, columnName2, columnValue2, idValue);
    }

    // 启用, 禁用
    public R enableBatchByIds (Collection<? extends Serializable> idList) { return R.ok().addData(CollectionUtils.isEmpty(idList) ? 0 : heMapper.enableBatchByIds(idList)); }
    public R disableBatchByIds(Collection<? extends Serializable> idList) { return R.ok().addData(CollectionUtils.isEmpty(idList) ? 0 : heMapper.disableBatchByIds(idList));}

    // 验证唯一性
    public void validUnique(String columnName, String columnValue, String idValue, String err) {
        long count = heMapper.countByColumnValue(columnName, columnValue, idValue);
        if (count > 0) throw new HeException(err);
    }
    public void validUnique(String columnName1, String columnValue1, String columnName2, String columnValue2, String idValue, String err) {
        long count = heMapper.countByTwoColumnValue(columnName1, columnValue1, columnName2, columnValue2, idValue);
        if (count > 0) throw new HeException(err);
    }

    // 查询自动选择是否分页
    public R listR(Long pageNum, Long pageSize) {return listR(pageNum, pageSize, null);}
    public R listR(Long pageNum, Long pageSize, Wrapper<T> queryWrapper) {
        R result = R.ok();
        if (pageNum == null || pageSize == null) {
            return result.addData(this.list(queryWrapper));
        } else {
            return result.addData(this.page(new Page<T>(pageNum, pageSize), queryWrapper));
        }
    }

    public R getRById(Serializable id) {
        T entity = this.getById(id);
        return R.ok().addData(entity);
    }

    // 新增, 编辑 带上时间和用户
    public R addR(T entity) { heMapper.addWithCreate(entity); return R.ok();}
    public R editR(T entity) { heMapper.editWithUpdate(entity); return R.ok();}
    public R deleteR(Collection<? extends Serializable> idList) { this.removeByIds(idList);return R.ok();}

}
