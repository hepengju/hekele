package com.hepengju.hekele.base.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 接口调用失败的通用简单粗暴处理类
 *
 * <br> 备注: 仅仅打印下调用服务的方法与参数，返回null
 * <br> 原因: 项目中很多时候的降级处理就没写, 或有些就打印个日志. 或服务名修改了, 新增方法了, 每次新增无意义代码. 此时可以使用此类
 *
 * @author hepengju
 */
@Slf4j
public class FeignFallbackUtil {

    public static <T> T get(Class<T> feignClientInterface, Throwable cause) {
        FeignClient annotation = feignClientInterface.getAnnotation(FeignClient.class);
        String serviceName = annotation.value();
        return (T) Proxy.newProxyInstance(feignClientInterface.getClassLoader()
                , new Class[]{feignClientInterface}
                , new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        log.error("当你看到这条信息，说明服务[{}.{}.{}]已经停止或不可访问，参数为：{}", serviceName, feignClientInterface.getSimpleName(), method.getName(), args, cause);
                        return null;
                    }
                });
    }

}
