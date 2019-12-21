package com.hepengju.hekele.base.config.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 禁用
 */
public class DisableById extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlTemplate = "<script>\nUPDATE %s SET enable_flag = '0' WHERE %s = #{id}\n</script>";
        String sql = String.format(sqlTemplate, tableInfo.getTableName(), tableInfo.getKeyColumn());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, "disableById", sqlSource);
    }
}

