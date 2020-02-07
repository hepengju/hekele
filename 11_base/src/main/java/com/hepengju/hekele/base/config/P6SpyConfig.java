package com.hepengju.hekele.base.config;

import com.p6spy.engine.spy.P6DataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import javax.sql.DataSource;

/**
 * P6Spy数据源装饰
 *
 * <pre>
 *     日期: 20190902
 *     作者: 何鹏举
 *     说明: 打印真实SQL语句(替换好变量), SQL耗时及SQL语句过滤等作用(参考项目下的p6spy.properties)
 *
 *     修正: 20190924
 *     说明: 发现在web-zuul中的jpa接口没有打印相关SQL日志, 检查发现是先进行了jpa自动配置, 跳过了此后置处理器.
 *           因此改为实现PriorityOrdered接口
 * </pre>
 */
@Configuration
public class P6SpyConfig {

    /**
     * P6数据源包装, 打印SQL语句
     */
    @Bean
    public P6DataSourceBeanPostProcessor p6DataSourceBeanPostProcessor() {
        return new P6DataSourceBeanPostProcessor();
    }

    class P6DataSourceBeanPostProcessor implements BeanPostProcessor, PriorityOrdered {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof DataSource) {
                return new P6DataSource((DataSource) bean);
            }
            return bean;
        }

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }
    }

}
