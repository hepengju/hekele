package com.hepengju.hekele.base.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.hepengju.hekele.base.config.mybatisplus.HeSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean // 扩展的SQL注入器
    public HeSqlInjector heSqlInjector(){ return new HeSqlInjector();}

    @Bean // MyBatisPlus的Page拦截器
    public PaginationInterceptor paginationInterceptor() { return new PaginationInterceptor(); }
}
