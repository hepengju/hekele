package com.hepengju.hekele.data.service;

import com.hepengju.hekele.data.GeneratorScan;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.meta.GeneratorMeta;
import com.hepengju.hekele.data.param.GeneratorParam;
import com.hepengju.hekele.data.util.GeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service @Slf4j
public class GeneratorService{

    private List<Generator> generatorList = GeneratorScan.getGeneratorList();

    /**
     * 获取生成器
     */
    public Map<String, List<GeneratorMeta>> getGenMap() {
        return generatorList.stream()
                .map(Generator::toGeneratorMeta)
                .collect(Collectors.groupingBy(GeneratorMeta::getType));
    }

    /**
     * 获取数据
     */
    public List<Map<String,String>> fetchDataByGenMeta(GeneratorParam param) {
        int sampleSize = param.getSampleSize();
        List<GeneratorMeta> metaList = param.getGeneratorMetaList();
        List<String> columnKeyList = metaList.stream().map(GeneratorMeta::getColumnKey).collect(toList());
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
     * 下载数据
     */
    public void downloadByGenMeta(GeneratorParam param) {
        List<GeneratorMeta> metaList = param.getGeneratorMetaList();
        List<String> columnNameList = metaList.stream().map(GeneratorMeta::getColumnName).collect(toList());
        List<Generator> genList = metaList.stream().map(GeneratorMeta::toGenerator).collect(toList());
        List<List<String>> dataList = GeneratorUtil.getDataStringList(genList, param.getSampleSize());
        GeneratorUtil.handleDataList(dataList, param.getFileFormat(), param.getTableName(), columnNameList);
    }
}
