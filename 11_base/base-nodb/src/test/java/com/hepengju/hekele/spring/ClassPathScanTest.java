package com.hepengju.hekele.spring;

import io.swagger.annotations.ApiModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

public class ClassPathScanTest {

    @Test
    public void testScan(){
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(ApiModel.class));
        Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents("com.hepengju");
        beanDefinitions.forEach(System.out::println);
    }

//    @Test
//    public void testMainApp() {
//        System.out.println(HekeleApplication.class.getPackage().getName());
//    }
}
