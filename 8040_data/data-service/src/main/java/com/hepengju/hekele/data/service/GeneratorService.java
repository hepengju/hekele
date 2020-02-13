package com.hepengju.hekele.data.service;

import com.alibaba.fastjson.JSON;
import com.hepengju.hekele.DataServiceApp;
import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.data.dto.GeneratorDTO;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.meta.MetaGenerator;
import com.hepengju.hekele.data.util.GeneratorUtil;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service @Slf4j
public class GeneratorService{

    private final String TYPE_DATE   = "date";
    private final String TYPE_NUMBER = "number";
    private final String TYPE_STRING = "string";
    private final String TYPE_CUSTOM = "custom";

    private final int SAMPLE_SIZE = 10;

    private List<String>                            genClassNameList = new ArrayList<>();
    @Getter private List<GeneratorDTO>              genList = new ArrayList<>();
    @Getter private Map<String, List<GeneratorDTO>> genMap;

    @PostConstruct
    public void init() {
        // 1.找出所有Generator接口的实现类
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Generator.class));
        Set<BeanDefinition> generators = provider.findCandidateComponents(DataServiceApp.class.getPackage().getName());

        // 2. 仅仅注入含有ApiModel注解的类
        int count = 0;
        try {
            for (BeanDefinition beanDefinition : generators) {
                String className = beanDefinition.getBeanClassName();
                Class<?> clazz = Class.forName(className);
                ApiModel apiModel = clazz.getAnnotation(ApiModel.class);
                if (apiModel != null) {
                    genClassNameList.add(className);
                    GeneratorDTO dto = new GeneratorDTO();
                    dto.setClassName(clazz.getName());
                    String desc = apiModel.value();
                    dto.setDesc(desc.replace("生成器",""));
                    Generator generator = (Generator) Class.forName(className).newInstance();
                    dto.setSampleData(generator.generateList(SAMPLE_SIZE));

                    String packageName = clazz.getPackage().getName();
                    if (packageName.endsWith(TYPE_DATE)) {          dto.setType(TYPE_DATE);
                    } else if (packageName.endsWith(TYPE_NUMBER)) { dto.setType(TYPE_NUMBER);
                    } else if (packageName.endsWith(TYPE_STRING)) { dto.setType(TYPE_STRING);
                    } else {                                        dto.setType(TYPE_CUSTOM);
                    }
                    genList.add(dto);
                    count++;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
        }

        log.info("auto find generator count: {}", count);
        genMap = genList.stream().collect(Collectors.groupingBy(GeneratorDTO::getType));
    }

    /**
     * 刷新样例数据
     */
    @SneakyThrows
    public void refreshSampleData() {
        for (GeneratorDTO dto : genList) {
            Generator generator = (Generator) Class.forName(dto.getClassName()).newInstance();
            dto.setSampleData(generator.generateList(SAMPLE_SIZE));
        }
        genMap = genList.stream().collect(Collectors.groupingBy(GeneratorDTO::getType));
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
