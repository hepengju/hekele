package com.hepengju.hekele.base.util;

import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.base.core.sqlhandle.SelectResult;
import com.hepengju.hekele.base.core.sqlhandle.SqlResult;
import com.p6spy.engine.spy.P6DataSource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 简易数据库工具
 * 
 * <li>功能: 取得数据库连接/释放数据库连接等
 * <li>场景: 开发阶段测试数据库连接,为BOUtil提供的方法,根据表名获得字段名/字段类型/字段注释等
 * 
 * <br>备注: 关于conn,pstmt,rst的释放: 工具内部生成的由工具释放，外面作为参数传入的内部不处理
 * 
 *	@author he_pe 2018-01-11
 */
public class DBUtil {

	private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);
	
	public static final String dbtype   = PropUtil.get("system.dbtype");
	public static final String url      = PropUtil.get("system.jdbc.url");
	public static final String username = PropUtil.get("system.jdbc.username");
	public static final String password = Base64Util.decode(PropUtil.get("system.jdbc.password"));
	
	public static final String url2      = PropUtil.getString("second.jdbc.url").contains("${") ? url : PropUtil.get("second.jdbc.url");
	public static final String username2 = PropUtil.getString("second.jdbc.username").contains("${") ? username : PropUtil.get("second.jdbc.username");
	public static final String password2 = PropUtil.getString("second.jdbc.password").contains("${") ? password : Base64Util.decode(PropUtil.get("second.jdbc.password"));
	
	public static final BigDecimal JS_NUMBER_MAX_VALUE = new BigDecimal("9007199254740992");
	
	/* 以下信息配置在Code表
	public static Map<String,String> driverClassMap = new HashMap<>();
	public static Map<String,String> jdbcUrlMap = new HashMap<>();
	
	static {
		driverClassMap.put("MySQL"      , "com.mysql.jdbc.Driver");
		    jdbcUrlMap.put("MySQL"      , "jdbc:mysql://<server>:<port3306>/<database>");
		 
		driverClassMap.put("DB2"        , "com.ibm.db2.jcc.DB2Driver");
		    jdbcUrlMap.put("DB2"        , "jdbc:db2://<server>:<port50000>/<database>");
		    
	    driverClassMap.put("Oracle"     , "oracle.jdbc.OracleDriver");
		    jdbcUrlMap.put("Oracle"     , "jdbc:oracle:thin:@<server>:<port1521>:<sid>");

	    driverClassMap.put("SqlServer"  , "com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        jdbcUrlMap.put("SqlServer"  , "jdbc:sqlserver://<server>:<port1433>;DatabaseName=<database>");
	        
	    driverClassMap.put("PostgreSQL"  , "org.postgresql.Driver");
	        jdbcUrlMap.put("PostgreSQL"  , "jdbc:postgresql://<server>:<port5432>/<database>");
	        
	    driverClassMap.put("MariaDB"     , "org.mariadb.jdbc.Driver");
	        jdbcUrlMap.put("MariaDB"     , "jdbc:mariadb://<server>:<port3306>/<database>");
	        
	    driverClassMap.put("GreenPlum"   , "com.pivotal.jdbc.GreenplumDriver");
	        jdbcUrlMap.put("GreenPlum"   , "jdbc:greenplum://<server>:<port5432>/<database>");
	}
	*/
	
    /**
     * 执行SQL语句
     */
    public static void execSql(String sqls, Properties prop, Connection conn) throws SQLException {
        sqls = StringUtil.replaceVar(sqls, prop);
        List<String> sqlList = SqlUtil.splitSQL(sqls);
        for (String sql : sqlList) {
            try(PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.execute();
            }
        }
    }
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// 测试使用
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~	
	/**
	 * 取得数据库连接
	 */
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			logger.error(StackUtil.getStackTrace(e));
		}
		Objects.requireNonNull(conn);
		return conn;
	}
	
	/**
	 * 取得数据库连接
	 */
	public static Connection getConnection2(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url2, username2, password2);
		} catch (Exception e) {
			logger.error(StackUtil.getStackTrace(e));
		}
		Objects.requireNonNull(conn);
		return conn;
	}
	
	/**
	 * 释放数据库连接
	 */
	public static void releaseConnection(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				logger.error(StackUtil.getStackTrace(e));
			}
		}
	}
	
	/**
	 * 回滚数据库连接
	 */
	public static void rollbackConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				logger.error(StackUtil.getStackTrace(e));
			}
		}
	}
	
	/**
	 * 关闭结果集
	 */
	public static void closeResultSet(ResultSet rst) {
		if (rst != null) {
			try {
				if (!rst.isClosed())
					rst.close();
			} catch (SQLException e) {
				logger.error(StackUtil.getStackTrace(e));
			}
		}
	}
	
	/**
	 * 根据数据库表名称取得Map<字段名称,字段类型>
	 */
	public static Map<String, String> getColumnTypeMap(String tableName,boolean second) {
		Map<String, String> map = new LinkedHashMap<>();
		Connection conn = null;
		try {
			conn = second ? DBUtil.getConnection2() : DBUtil.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			String schema = null;
			tableName = tableName.toUpperCase();
			int dot = tableName.indexOf(".");
			if(dot != -1) {
				schema = tableName.substring(0, dot);
				tableName = tableName.substring(dot + 1);
			}
			try(ResultSet rst = metaData.getColumns(null, schema, tableName.toUpperCase(), "%")){
				while (rst.next()) {
					String columnName = rst.getString("COLUMN_NAME");
					String columnType = rst.getString("TYPE_NAME");
					map.put(columnName, columnType);
				}
			}
		} catch (SQLException e) {
			logger.error(StackUtil.getStackTrace(e));
		} finally {
			DBUtil.releaseConnection(conn);
		}
		return map;
	}

	/**
	 * 根据数据库表名称取得Map<字段名称,字段注释>
	 */
	public static Map<String, String> getColumnCommentMap(String tableName,boolean second) {
		Map<String, String> map = new LinkedHashMap<>();
		Connection conn = null;
		try {
			conn = second ? DBUtil.getConnection2() : DBUtil.getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			
			String schema = null;
			tableName = tableName.toUpperCase();
			int dot = tableName.indexOf(".");
			if(dot != -1) {
				schema = tableName.substring(0, dot);
				tableName = tableName.substring(dot + 1);
			}
			try(ResultSet rst = metaData.getColumns(null, schema, tableName.toUpperCase(), "%")){
				while (rst.next()) {
					String columnName = rst.getString("COLUMN_NAME");
					String columnRemark = rst.getString("REMARKS");
					map.put(columnName, columnRemark);
				}
			}
		} catch (SQLException e) {
			logger.error(StackUtil.getStackTrace(e));
		} finally {
			DBUtil.releaseConnection(conn);
		}
		return map;
	}
	
	/**
	 * 根据Sql语句取得Map<字段名称,字段类型>(sql.Type转换为使用的Type)
	 */
	public static Map<String, String> getColumnTypeMapBySql(Connection conn,String sql) {
		Map<String, String> map = new LinkedHashMap<>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setMaxRows(1);
			try (ResultSet rst = pstmt.executeQuery()) {
				ResultSetMetaData rstmd = rst.getMetaData();
				int columnCount = rstmd.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					String label = rstmd.getColumnLabel(i);
					int sqlType = rstmd.getColumnType(i);
					String reportType = sqlTypeToReportType(sqlType);
					map.put(label, reportType);
				}
			}
		} catch (SQLException e) {
			logger.error(StackUtil.getStackTrace(e));
		} 
		return map;
	}

	/**
	 * java.sql.Types转换为报表查询所需的类型
	 */
	public static String sqlTypeToReportType(int sqlType) {
		String javaType = "string";
		switch (sqlType) {
		case Types.BIT:
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.BIGINT:
			javaType="integer";
			break;
		case Types.FLOAT:
		case Types.REAL:	
		case Types.DOUBLE:	
		case Types.NUMERIC:	
		case Types.DECIMAL:	
			javaType="double";
			break;
		case Types.DATE:	
		case Types.TIME:	
		case Types.TIMESTAMP:		
			javaType="date";
		default:
			break;
		}
		return javaType;
	}

	
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Spring相关对象
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * 取得数据源: 测试临时使用, 开发请使用 @DbConfigService
	 */
	public static DataSource getDataSource() {
		//new DriverManagerDataSource(url, username, password);
		DataSource dataSource = new SingleConnectionDataSource(url, username, password, true);
		return new P6DataSource(dataSource);
	}
	
	/**
	 * 取得JDBCTemplate: 测试临时使用, 开发请使用 @DbConfigService
	 */
	public static JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}
	
	/**
	 * 取得SimpleJdbcInsert: 测试临时使用, 开发请使用 @DbConfigService
	 */
	public static SimpleJdbcInsert getSimpleJdbcInsert(String tableName) {
		return new SimpleJdbcInsert(getDataSource()).withTableName(tableName);
	}


	/**
	 * Spring的JdbcTemplate的QueryForList返回值的特殊处理
	 */
	public static List<Map<String,Object>> handleJdbcType(List<Map<String,Object>> list){
		return list.stream()
				   .map(map -> {
							 map.forEach((k,v) -> map.put(k,handleJdbcType(v)));
			                 return map;})
				   .collect(Collectors.toList());
	}
	
	/**
	 * 默认不处理double的精度
	 */
	public static Object handleJdbcType(Object obj) {
		return handleJdbcType(obj,false);
	}

	/**
	 * JDBC类型的特殊处理: 针对Web界面的SQL查询器
	 * 
	 * <br>分析:此时对于Double等小数位不用格式化处理, 由 handleJdbcTypeDouble 额外处理
	 * <br>JDBC的所有类型参见: @java.sql.Types
	 * <br>Mybatis的类型处理参见: http://www.mybatis.org/mybatis-3/zh/configuration.html#typeHandlers
	 */
	public static Object handleJdbcType(Object obj,boolean doubleTwoScale) {
		//日期/时间的格式化处理
		if(obj instanceof Time) {
			return DateFormatUtils.format((java.util.Date)obj, "yyyy-MM-dd HH:mm:ss");
		}else if(obj instanceof java.sql.Date) {
			return DateFormatUtils.format((java.sql.Date)obj, "yyyy-MM-dd");
		}else if(obj instanceof Timestamp) {
			return DateFormatUtils.format((Timestamp)obj, "yyyy-MM-dd HH:mm:ss");
		}else if(obj instanceof java.util.Date) {
			return DateFormatUtils.format((Time)obj, "yyyy-MM-dd HH:mm:ss");
		
		//Clob,Blob处理	20190214 情人节 何鹏举 处理Clob到字符串
		}else if(obj instanceof Blob) {
			return "(blob)";
		}else if(obj instanceof Clob) {
			Clob clob = (Clob) obj;
			try(Reader reader = clob.getCharacterStream()) {
				char[] cbuf = new char[(int) clob.length()];
				reader.read(cbuf);
				return new String(cbuf);
			} catch (SQLException | IOException e) {
				return "(clob)";
			}
			
		//空值处理
		}else if(obj == null) {
			return "";
		
		}else {
			if (doubleTwoScale) {
				if (obj instanceof Float) {
					return Double.parseDouble(String.format("%.2f", obj));
				} else if (obj instanceof Double) {
					return Double.parseDouble(String.format("%.2f", obj));
				} else if (obj instanceof BigDecimal) {
					return ((BigDecimal) obj).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
			}
			
			//20190219 王正英/何鹏举 对于BigDecimal当大于js的最大值: 2^53时, 返回字符串
			if (obj instanceof BigDecimal) {
				BigDecimal dbValue = (BigDecimal) obj;
				if(dbValue.compareTo(JS_NUMBER_MAX_VALUE) > 0) {
					return dbValue.toString();
				}
			}
			return obj;
		}
	}


	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Sql查询器相关
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * 获取Sql语句的类型
	 *
	 * 20190620 追加 Merge和Call
	 */
	public static String getSqlType(String sql){
		String newsql = sql.trim().toLowerCase();
		if(newsql.startsWith("select")) return "Select";
		if(newsql.startsWith("update")) return "Update";
		if(newsql.startsWith("delete")) return "Delete";
		if(newsql.startsWith("insert")) return "Insert";
		if(newsql.startsWith("merge"))  return "Merge";
		if(newsql.startsWith("call"))   return "Call";

		return "other";
	}
	
	public static SelectResult handleSelect(Connection conn, String sql, int maxRows) {
		SelectResult selectResult = new SelectResult();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			//设置最大行数 
			pstmt.setMaxRows(maxRows);
			try (ResultSet rst = pstmt.executeQuery()) {
				// 标题区
				ResultSetMetaData metaData = rst.getMetaData();
				int count = metaData.getColumnCount();
				List<String> titleList = new ArrayList<>();
				for (int i = 1; i <= count; i++) {
					String label = metaData.getColumnLabel(i); // 列标签
					titleList.add(label);
				}
				selectResult.setTitleList(titleList);
				
				// 数据区
				List<List<Object>> recordList = new ArrayList<>();
				while (rst.next()) {
					List<Object> row = new ArrayList<>();
					for (int i = 1; i <= count; i++) {
						Object oldValue = rst.getObject(i);
						Object newValue = DBUtil.handleJdbcType(oldValue); // JDBC类型处理
						row.add(newValue);
					}
					recordList.add(row);
				}
				selectResult.setRecordList(recordList);
			}
		} catch (SQLException e) {
			throw new HeException(e);
		}
		return selectResult;
	}
	/**
	 * SQL语句中Select执行
	 */
	public static SelectResult handleSelect(Connection conn, String sql) {
		return handleSelect(conn, sql, 0);
	}
	
	/**
	 * SQL语句中的Select Count(*) From ...
	 * 注意: MySQL返回的为Long类型,Oracle返回的为BigDecimal类型
	 */
	public static Number handleSelectCount(Connection conn,String countSql) {
		SelectResult handleSelect = handleSelect(conn, countSql, 1);
		return  (Number) handleSelect.getRecordList().get(0).get(0);
	}
	/**
	 * 处理增删改语句
	 */
	public static int handleUpdate(Connection conn, String sql) {
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			int rows = pstmt.executeUpdate();
			return rows;
		} catch (SQLException e) {
			throw new HeException(e);
		}
	}
	
	/**
	 * 处理Call语句
	 */
	public static SqlResult handleCall(Connection conn, String sql) {
		SqlResult sqlResult = new SqlResult();
		try(CallableStatement pstmt = conn.prepareCall(sql)){
			boolean execute = pstmt.execute();
			if(execute) {
				try(ResultSet rst = pstmt.getResultSet()){
					SelectResult selectResult = resultSetToSelectResult(rst);
					sqlResult.setSelectResult(selectResult);
				}
			}else {
				int updateCount = pstmt.getUpdateCount();
				sqlResult.setAffectedRows(updateCount);
			}
			return sqlResult;
		} catch (SQLException e) {
			throw new HeException(e);
		}
	}

	/**
	 * 处理其他语句(SqlResult中仅仅结果集和影响行数有值)
	 */
	public static SqlResult handleOther(Connection conn, String sql) {
		SqlResult sqlResult = new SqlResult();
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			boolean execute = pstmt.execute();
			if(execute) {
				try(ResultSet rst = pstmt.getResultSet()){
					SelectResult selectResult = resultSetToSelectResult(rst);
					sqlResult.setSelectResult(selectResult);
				}
			}else {
				int updateCount = pstmt.getUpdateCount();
				sqlResult.setAffectedRows(updateCount);
			}
			return sqlResult;
		} catch (SQLException e) {
			throw new HeException(e);
		}
	}

	public static SelectResult resultSetToSelectResult(ResultSet rst) throws SQLException {
		SelectResult selectResult = new SelectResult();
		// 标题区
		ResultSetMetaData metaData = rst.getMetaData();
		int count = metaData.getColumnCount();
		List<String> titleList = new ArrayList<>();
		for (int i = 1; i <= count; i++) {
			String label = metaData.getColumnLabel(i); // 列标签
			titleList.add(label);
		}
		selectResult.setTitleList(titleList);

		// 数据区
		List<List<Object>> recordList = new ArrayList<>();
		while (rst.next()) {
			List<Object> row = new ArrayList<>();
			for (int i = 1; i <= count; i++) {
				Object oldValue = rst.getObject(i);
				Object newValue = DBUtil.handleJdbcType(oldValue); // JDBC类型处理
				row.add(newValue);
			}
			recordList.add(row);
		}
		selectResult.setRecordList(recordList);
		return selectResult;
	}
	
//	/**
//	 * 根据jdbcUrl获取数据库类型
//	 */
//	public static String getDbType(String jdbcUrl) {
//		DBType dbType = JdbcUtils.getDbType(jdbcUrl);
//		//特殊处理
//		String dbTypeStr = dbType.getDb();
//		if("sqlserver2005".equalsIgnoreCase(dbTypeStr)) dbTypeStr = "sqlserver";
//		return dbTypeStr;
//	}
	
//	/**
//	 * 生成分页语句
//	 */
//	public static String buildPaginationSql(RowBounds rowBounds, String buildSql, DBType dbType) {
//		try {
//			return DialectFactory.buildPaginationSql(rowBounds, buildSql, dbType, null);
//		} catch (Exception e) {
//			throw new HeException(e);
//		}
//	}

	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// Spring相关对象
	//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	
	///**
	// * 根据数据库表名称取得Map<字段名称,字段类型>
	// * <br> 
	// * 采用[select * from tableName where 1 = 0]语句,是各种数据库的通用方法
	// */
	//public static Map<String, String> getColumnTypeMap(String tableName){
	//	Map<String, String> map = new LinkedHashMap<>();
	//	String sql = "SELECT * FROM " + tableName + " WHERE 1 = 0 ";
	//	Connection conn = DBUtil.getConnection();
	//	try (PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rst = pstmt.executeQuery();) {
	//		ResultSetMetaData metaData = rst.getMetaData();
	//		int count = metaData.getColumnCount();
	//		for (int i = 1; i <= count; i++) {
	//			String key = metaData.getColumnName(i);
	//			String value = metaData.getColumnTypeName(i);
	//			map.put(key, value);
	//		}
	//	} catch (SQLException e) {
	//		logger.error(StackUtil.getStackTrace(e));
	//	}
	//	DBUtil.releaseConnection(conn);
	//	return map;
	//}
}
