package com.hepengju.hekele.data.service;

import com.alibaba.fastjson.JSON;
import com.hepengju.hekele.DataServiceApp;
import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.data.filter.GeneratorFilter;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.meta.MetaGenerator;
import com.hepengju.hekele.data.util.GeneratorUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service @Slf4j
public class GeneratorService{

    private List<String>                             genClassNameList = new ArrayList<>();
    @Getter private List<MetaGenerator>              genList = new ArrayList<>();
    @Getter private Map<String, List<MetaGenerator>> genMap;

    @PostConstruct
    public void init() {
        // 1.找出所有Generator接口的实现类
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        //provider.addIncludeFilter(new AssignableTypeFilter(Generator.class));
        //provider.addIncludeFilter(new AnnotationTypeFilter(ApiModel.class));
        // 以上两个包含过滤器是并集的关系，此处的需求是交集
        provider.addIncludeFilter(new GeneratorFilter());

        Set<BeanDefinition> generators = provider.findCandidateComponents(DataServiceApp.class.getPackage().getName());

        // 2. 仅仅注入含有ApiModel注解的类
        int count = 0;
        try {
            for (BeanDefinition beanDefinition : generators) {
                String className = beanDefinition.getBeanClassName();
                Class<?> clazz = Class.forName(className);
                Generator generator = (Generator) clazz.newInstance();
                MetaGenerator metaGenerator = generator.toMetaGenerator();
                genList.add(metaGenerator);
                count++;
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        }

        log.info("auto find generator count: {}", count);
        genClassNameList = genList.stream().map(MetaGenerator::getClassName).collect(Collectors.toList());
        genMap = genList.stream().collect(Collectors.groupingBy(MetaGenerator::getType));
    }

    /**
     * 刷新样例数据
     */
    public void refreshSampleData() {
        for (MetaGenerator metaGenerator : genList) {
            metaGenerator.setSampleData(metaGenerator.toGenerator().generateList(MetaGenerator.SAMPLE_SIZE));
        }
        genMap = genList.stream().collect(Collectors.groupingBy(MetaGenerator::getType));
    }

    /**
     * 生成样例数据(一个)
     */
    public List<Object> getSampleDataByMeta(MetaGenerator metaGenerator, int sampleSize) {
        if (!genClassNameList.contains(metaGenerator.getClassName())) throw new HeException("data.className.notExist", metaGenerator.getClassName());
        return metaGenerator.toGenerator().generateList(sampleSize);
    }

    /**
     * 生成样例数据(多个)
     */
    public List<List<Object>> getSampleDataTableByMeta(String metaGeneratorJsonArr, int sampleSize) {
        List<MetaGenerator> metaGeneratorList = JSON.parseArray(metaGeneratorJsonArr, MetaGenerator.class);
        List<List<Object>> result = new ArrayList<>();
        for (MetaGenerator metaGenerator : metaGeneratorList) {
            result.add(metaGenerator.toGenerator().generateList(sampleSize));
        }
        return result;
    }

    /**
     * 下载样例数据
     */
    public void downloadSampleDataTable(String metaGeneratorJsonArr, int sampleSize, String dataFormat, String tableName, String columnNames) {
        List<String> columnNameList = Arrays.asList(columnNames.split(","));
        if("sql".equals(dataFormat)){
            List<MetaGenerator> metaGeneratorList = JSON.parseArray(metaGeneratorJsonArr, MetaGenerator.class);
            if(metaGeneratorList.size() != columnNameList.size()) throw new HeException("data.columnCount.error");
        }
        List<List<Object>> dataList = getSampleDataTableByMeta(metaGeneratorJsonArr, sampleSize);
        GeneratorUtil.handleDataList(dataList, dataFormat, tableName, columnNameList);
    }
}
