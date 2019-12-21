package com.hepengju.hekele.base.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * SpringMvc的配置
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {

    /**
     * fastjson提高 @RestController @ResponseBody @RequestBody 注解的 JSON序列化速度
     */
    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        //自定义配置...
        //FastJsonConfig config = new FastJsonConfig();
        //config.set ...
        //converter.setFastJsonConfig(config);
        converters.add(0, converter);
    }
}
