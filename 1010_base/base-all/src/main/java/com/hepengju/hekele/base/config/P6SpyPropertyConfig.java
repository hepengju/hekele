package com.hepengju.hekele.base.config;

import java.util.Properties;

/**
 * 不想在每次微服务下都放置一个spy.properties, 因此在base中中用环境变量集中配置
 *
 * @see P6SpyConfig 在发现数据源的时候, 调用init方法初始化下系统属性
 * @author hepengju
 */
public class P6SpyPropertyConfig {

    private static final String PRE = "p6spy.config.";

    /**
     * 初始化设置
     */
    public static void init(){
        Properties prop = System.getProperties();
        prop.put(PRE + "autoflush"                      , "false");
        prop.put(PRE + "dateformat"                     , "yyyy-MM-dd HH:mm:ss");
        prop.put(PRE + "appender"                       , "com.p6spy.engine.spy.appender.Slf4JLogger");
        prop.put(PRE + "logMessageFormat"               , "com.p6spy.engine.spy.appender.CustomLineFormat");
        prop.put(PRE + "customLogMessageFormat"         , "%(executionTime)ms | %(sqlSingleLine)");
        prop.put(PRE + "databaseDialectDateFormat"      , "yyyy-MM-dd");
        prop.put(PRE + "databaseDialectTimestampFormat" , "yyyy-MM-dd HH:mm:ss");
        prop.put(PRE + "databaseDialectBooleanFormat"   , "boolean");
        prop.put(PRE + "filter"                         , "true");
        prop.put(PRE + "exclude"                        , "^SELECT 1");
        prop.put(PRE + "excludecategories"              , "info,debug,result,resultset,batch,commit,rollback");
    }
}
