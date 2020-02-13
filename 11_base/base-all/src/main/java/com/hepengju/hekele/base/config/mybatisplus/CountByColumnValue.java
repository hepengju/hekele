package com.hepengju.hekele.base.config.mybatisplus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 查询某列指定值的个数
 *
 * <br> 用于新增或编辑时判断用户账号, 角色代码等的唯一性
 */
public class CountByColumnValue extends AbstractMethod {
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sqlTemplate = "<script>\nSELECT COUNT(1) FROM %s WHERE ${columnName}=#{columnValue} %s " +
                " <if test=\"id != null and id != ''\"> AND %s = #{id}</if>\n</script>";
        String sql = String.format(sqlTemplate
                , tableInfo.getTableName()
                , (tableInfo.isLogicDelete() ? tableInfo.getLogicDeleteSql(true, true) : "")
                , tableInfo.getKeyColumn()
        );
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, "countByColumnValue", sqlSource, Long.class);
    }
}
