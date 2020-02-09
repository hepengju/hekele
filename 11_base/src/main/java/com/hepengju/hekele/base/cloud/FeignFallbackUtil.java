package com.hepengju.hekele.base.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 接口调用失败的通用处理类
 *
 * <br> 打印下方法与参数，返回null
 *
 * @author hepengju
 */
@Slf4j
public class FeignFallbackUtil {

    public static <T> T get(Class<T> feignClientInterface, Throwable cause) {
        FeignClient annotation = feignClientInterface.getAnnotation(FeignClient.class);
        String serviceName = annotation.value();
        return (T) Proxy.newProxyInstance(feignClientInterface.getClass().getClassLoader()
                    , new Class[]{feignClientInterface}
                    , new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            log.error("当你看到这条信息，说明服务[{}]已经停止或不可访问，参数为：[{}]", serviceName, args, cause);
                            return null;
                        }
                    });
    }

}
