package com.hepengju.hekele.data;

import com.hepengju.hekele.data.generator.Generator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.List;
import java.util.Set;

public class ClassPathScanTest {

    @Test
    public void testMyScan() {
        List<Generator> genList = GeneratorScan.getGeneratorList();
        genList.forEach(g -> System.out.println(g.getClass()));
    }

    @Test
    public void testScan() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Generator.class));
        Set<BeanDefinition> beanDefinitions = provider.findCandidateComponents("com.hepengju");
        beanDefinitions.forEach(System.out::println);
    }

}
