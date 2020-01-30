package com.hepengju.hekele.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * SpringMVC的配置
 */
@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    /**
     * 允许跨域
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        List<String> allowedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest");
        List<String> exposedHeaders = Arrays.asList("x-auth-token", "content-type", "X-Requested-With", "XMLHttpRequest");
        List<String> allowedMethods = Arrays.asList("POST", "GET", "DELETE", "PUT", "OPTIONS");
        List<String> allowedOrigins = Arrays.asList("*");
        corsConfig.setAllowedHeaders(allowedHeaders);
        corsConfig.setAllowedMethods(allowedMethods);
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setExposedHeaders(exposedHeaders);
        corsConfig.setMaxAge(36000L);
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", corsConfig);
        return new CorsFilter(configSource);
    }

    /**
     * fastjson提高 @RestController @ResponseBody @RequestBody 注解的 JSON序列化速度
     *
     * 异常: Content-Type cannot contain wildcard type '*'
     * 解决: 手动配置支持的MediaType类型, 但是比较麻烦
     *      另外fastjson序列化默认忽略null属性, 也需要配置才行
     *      还有一点关键的是, fastjson序列化后的json按照字母排序了, 不能设置按照默认的顺序
     *      所以还是使用jackson吧!
     */
    //    @Override
    //    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    //        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
    //        //自定义配置...
    //        //FastJsonConfig config = new FastJsonConfig();
    //        //config.set ...
    //        //converter.setFastJsonConfig(config);
    //        converters.add(0, converter);
    //    }
}
