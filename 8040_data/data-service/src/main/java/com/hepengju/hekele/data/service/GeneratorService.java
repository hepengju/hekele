package com.hepengju.hekele.data.service;

import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.data.GeneratorScan;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.meta.GeneratorMeta;
import com.hepengju.hekele.data.param.GeneratorParam;
import com.hepengju.hekele.data.util.GeneratorUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service @Slf4j @Data
public class GeneratorService{

    private List<Generator> genList;
    private Map<String, GeneratorMeta> metaMap;

    @PostConstruct
    public void init(){
        genList = GeneratorScan.getGeneratorList();
        metaMap = new LinkedHashMap<>(genList.size() * 2);
        List<GeneratorMeta> metaList = genList.stream().map(Generator::toGeneratorMeta).collect(toList());
        for (GeneratorMeta meta : metaList) {
            metaMap.put(meta.getName(), meta);  // chineseName
            metaMap.put(meta.getColumnTitle(), meta);  // 中文姓名
        }
    }

    /**
     * 获取生成器
     */
    public Map<String, List<GeneratorMeta>> getGenMap() {
        return genList.stream()
                .map(Generator::toGeneratorMeta)
                .collect(Collectors.groupingBy(GeneratorMeta::getType));
    }

    private GeneratorMeta nameToMeta(String name) {
        GeneratorMeta meta = metaMap.get(name);
        if(meta == null) throw new HeException("生成器[%s]不存在", name);
        return meta;
    }

    private List<GeneratorMeta> paramToMetaList(GeneratorParam param) {
        List<GeneratorMeta> metaList = param.getMetaList();
        for (GeneratorMeta meta : metaList) {
            String className = nameToMeta(meta.getName()).getClassName();
            meta.setClassName(className);
        }
        return metaList;
    }

    /**
     * 获取数据
     */
    public List<String> getData(String name, int sampleSize) {
        // GeneratorMeta meta = nameToMeta(name);

        // 准确匹配
        GeneratorMeta meta = metaMap.get(name);

        // 模糊匹配
        if (meta == null) {
            for (String key : metaMap.keySet()) {
                if(name.toLowerCase().contains(key.toLowerCase())) {
                    meta = metaMap.get(key);
                }
            }
        }
        return meta.toGenerator().generateStringList(sampleSize);
    }

    /**
     * 刷新表格
     */
    public List<Map<String,String>> refreshTable(GeneratorParam param) {
        int sampleSize = param.getSampleSize();
        List<String> columnKeyList = param.getMetaList().stream().map(GeneratorMeta::getColumnKey).collect(toList());
        List<GeneratorMeta> metaList = paramToMetaList(param);
        List<Generator> genList = metaList.stream().map(GeneratorMeta::toGenerator).collect(toList());

        List<Map<String,String>> result = new ArrayList<>(sampleSize);
        for (int i = 0; i < sampleSize; i++) {
            Map<String,String> rowMap  = new LinkedHashMap<>(metaList.size());
            for (int j = 0; j < genList.size(); j++) {
                String key = columnKeyList.get(j);
                String value = genList.get(j).generateString();
                rowMap.put(key, value);
            }
            result.add(rowMap);
        }
        return result;
    }

    /**
     * 下载表格
     */
    public void downTable(GeneratorParam param) {
        List<GeneratorMeta> metaList = paramToMetaList(param);
        List<String> columnTitleList = metaList.stream().map(GeneratorMeta::getColumnTitle).collect(toList());
        List<String> columnNameList = metaList.stream().map(GeneratorMeta::getColumnName).collect(toList());
        List<Generator> genList = metaList.stream().map(GeneratorMeta::toGenerator).collect(toList());
        List<List<String>> dataList = GeneratorUtil.getDataStringList(genList, param.getSampleSize());
        GeneratorUtil.handleDataList(dataList, param.getFileFormat(), param.getFileName(), param.getTableName()
                , columnTitleList, columnNameList);
    }


}
