package com.hepengju.hekele.base.config;

import com.hepengju.hekele.base.config.mybatisplus.HeSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public HeSqlInjector heSqlInjector(){
        return new HeSqlInjector();
    }

}
