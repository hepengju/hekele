package com.hepengju.hekele.data;

import com.hepengju.hekele.data.filter.GeneratorFilter;
import com.hepengju.hekele.data.generator.Generator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 扫描此类所在包下面的所有生成器并排序
 *
 * @author hepengju
 */
@Slf4j
public class GeneratorScan {

    public static List<Generator> getGeneratorList(){
        List<Generator> genList = new ArrayList<>();

        // 1.找出所有Generator接口的实现类
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new GeneratorFilter());
        Set<BeanDefinition> genBeanSet = provider.findCandidateComponents(GeneratorScan.class.getPackage().getName());

        // 2. 转换为List进行排序
        List<BeanDefinition> genBeanList = genBeanSet.stream().collect(Collectors.toList());
        AnnotationAwareOrderComparator.sort(genBeanList);

        int count = 0;
        try {
            for (BeanDefinition beanDefinition : genBeanList) {
                String className = beanDefinition.getBeanClassName();
                Class<?> clazz = Class.forName(className);
                Generator generator = (Generator) clazz.newInstance();
                genList.add(generator);
                count++;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        }

        log.info("auto find generator count: {}", count);
        return genList;
    }
}
