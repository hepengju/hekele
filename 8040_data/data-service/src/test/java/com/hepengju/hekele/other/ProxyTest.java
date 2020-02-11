package com.hepengju.hekele.other;

import com.hepengju.hekele.data.generator.Generator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyTest {

    @Test
    public void testJdkDynamicProxy() {
        Generator generator = getProxy(Generator.class);
        generator.generate();
    }

    public<T> T getProxy(Class<T> clazz) {
        return (T)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println(method.getName() + ": " + args);
                return null;
            }
        });
    }
}
