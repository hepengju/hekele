package com.hepengju.hekele.data.web;

import com.alibaba.fastjson.JSON;
import com.hepengju.hekele.HekeleApplication;
import com.hepengju.hekele.base.core.Now;
import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.base.util.ExcelUtil;
import com.hepengju.hekele.base.util.PrintUtil;
import com.hepengju.hekele.base.util.WebUtil;
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
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.hepengju.hekele.data.util.GeneratorUtil.getGeneratorByClassName;
import static com.hepengju.hekele.data.util.GeneratorUtil.getSampleData;

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
        Set<BeanDefinition> generators = provider.findCandidateComponents(HekeleApplication.class.getPackage().getName());

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
                    dto.setDesc(apiModel.value());
                    dto.setSampleData(getSampleData(getGeneratorByClassName(className), SAMPLE_SIZE));

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
        } catch (ClassNotFoundException e) {
        }

        genMap = genList.stream().collect(Collectors.groupingBy(GeneratorDTO::getType));
    }

    /**
     * 刷新样例数据
     */
    public void refreshSampleData() {
        for (GeneratorDTO dto : genList) {
            dto.setSampleData(getSampleData(getGeneratorByClassName(dto.getClassName()), SAMPLE_SIZE));
        }
        genMap = genList.stream().collect(Collectors.groupingBy(GeneratorDTO::getType));
    }

    /**
     * 生成样例数据(一个)
     */
    public List<Object> getSampleDataByMeta(MetaGenerator metaGenerator, int sampleSize) {
        if (!genClassNameList.contains(metaGenerator.getClassName())) throw new HeException("data.className.notExist", metaGenerator.getClassName());
        return getSampleData(metaGenerator.toGenerator(), sampleSize);
    }

    /**
     * 生成样例数据(多个)
     */
    public List<List<Object>> getSampleDataTableByMeta(String metaGeneratorJsonArr, int sampleSize) {
        List<MetaGenerator> metaGeneratorList = JSON.parseArray(metaGeneratorJsonArr, MetaGenerator.class);
        List<List<Object>> result = new ArrayList<>();
        for (MetaGenerator metaGenerator : metaGeneratorList) {
            result.add(getSampleDataByMeta(metaGenerator, sampleSize));
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
        handleDataList(dataList, dataFormat, tableName, columnNameList);
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @SneakyThrows
    public void downloadDataByClassFullName(String classFullName, Integer count, String dataFormat) {
        Class<?> clazz = Class.forName(classFullName);
        List<List<Object>> dataList = GeneratorUtil.getDataList(clazz, count);

        String tableName = GeneratorUtil.getTableName(clazz);
        List<String> columnNameList = GeneratorUtil.getColumnNameList(clazz);
        handleDataList(dataList, dataFormat, tableName, columnNameList);
    }

    @SneakyThrows
    private void handleDataList(List<List<Object>> dataList, String dataFormat, String tableName, List<String> columnNameList){
        String fileName = tableName + "-" + Now.yyyyMMddHHmmss();
        if ("sql".equals(dataFormat)) {
            String result = PrintUtil.printInsert(tableName, columnNameList, dataList, true);
            WebUtil.handleFileDownload(fileName + ".sql", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("csv".equals(dataFormat)) {
            String result = PrintUtil.printCSV(dataList);
            WebUtil.handleFileDownload(fileName + ".csv", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("tsv".equals(dataFormat)) {
            String result = PrintUtil.printTSV(dataList);
            WebUtil.handleFileDownload(fileName + ".tsv", result.getBytes(StandardCharsets.UTF_8));
            return;
        }

        if ("excel".equals(dataFormat)) {
            WebUtil.handleFileDownload(fileName + ".xlsx");
            ExcelUtil.exportFromList(columnNameList, dataList, WebUtil.getHttpServletResponse().getOutputStream());
            return;
        }

        throw new HeException("格式暂不支持");
    }
}
