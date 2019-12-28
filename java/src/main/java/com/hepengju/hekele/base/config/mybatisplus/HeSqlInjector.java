package com.hepengju.hekele.base.config.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

/**
 * 扩展MyBatisPlus自带的方法
 */
public class HeSqlInjector extends DefaultSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new EnableBatchByIds());
        methodList.add(new DisableBatchByIds());
        methodList.add(new CountByColumnValue());
        return methodList;
    }
}
