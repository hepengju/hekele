package com.hepengju.hekele.base.config.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 批量禁用
 */
public class DisableBatchByIds extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlTemplate = "<script>\nUPDATE %s SET enable_flag = '0' WHERE %s IN (%s)\n</script>";
        String sql = String.format(sqlTemplate, tableInfo.getTableName(), tableInfo.getKeyColumn(),
                SqlScriptUtils.convertForeach("#{item}", COLLECTION, null, "item", COMMA));
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, "disableBatchByIds", sqlSource);
    }
}

