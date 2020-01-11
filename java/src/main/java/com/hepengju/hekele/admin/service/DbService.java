package com.hepengju.hekele.admin.service;

import com.hepengju.hekele.base.util.SqlUtil;
import com.hepengju.hekele.base.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Service
public class DbService {

    @Autowired private DataSource dataSource;

    /**
     * 获取一个数据库连接
     */
    public Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行SQL语句
     */
    public void execSql(Connection conn, String sqls, Properties prop) {
        if (StringUtils.isBlank(sqls)) return;
        if (prop != null) sqls = StringUtil.replaceVar(sqls, prop);

        try {
            List<String> sqlList = SqlUtil.splitSQL(sqls);
            conn.setAutoCommit(false);
            for (String sql : sqlList) {
                try (PreparedStatement ps = conn.prepareStatement(sql);) {
                    ps.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
