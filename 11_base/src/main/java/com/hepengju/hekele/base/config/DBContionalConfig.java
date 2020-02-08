package com.hepengju.hekele.base.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 *
 * TODO: 有些服务不需要连接数据库, 那么依赖数据库的几个公共Bean, 不用加入到容器中.
 *       比如data或可能存在的xxx-web模板引擎替换前端独立js, 它们可以如下配置
 *
 * 说明: 由于base项目中有CodeMapper, ExcelController等, 不方便进行排除. 先这样吧
 *
 * <pre>
 *     1. 排除 DataSourceAutoConfiguration
 *     2. 此时以下配置自动生效, 因为 @ConditionalOnBean(DataSource.class)
 * </pre>
 */
@Configuration @ConditionalOnBean(DataSource.class)
public class DBContionalConfig {

}
