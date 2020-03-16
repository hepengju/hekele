package com.hepengju.hekele.base.filter;

import com.hepengju.hekele.base.core.Now;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 当前用户拦截器
 *
 * zuul转发, feign调用之后都可以获取当前用户
 *
 * zuul: https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/2.0.4.RELEASE/single/spring-cloud-netflix.html#_custom_zuul_filter_examples
 * feign:
 */
public class NowUserFilter extends ZuulFilter implements RequestInterceptor {

    @Override public String  filterType()   { return PRE_TYPE;}
    @Override public int     filterOrder()  { return PRE_DECORATION_FILTER_ORDER - 1;}
    @Override public boolean shouldFilter() { return true; }

    @Override
    public Object run() throws ZuulException {
        return null;
    }

    // feign的请求拦截
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(Now.NOW_USER_FEIGN, Now.user().toString());
    }
}
