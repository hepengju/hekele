package com.hepengju.hekele.data.client;

import com.hepengju.hekele.base.cloud.FeignClientConst;
import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.data.meta.MetaGenerator;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * 参看: com.hepengju.hekele.data.web.GeneratorController
 */
@FeignClient(value = FeignClientConst.DATA_SERVICE, fallbackFactory = DataClientFallbackFactory.class)
@RequestMapping("/data/")
public interface DataClient {

    @GetMapping("getGenList")
    JsonR getGenList();

    @GetMapping("getGenMap")
    JsonR getGenMap();

    @PostMapping("refreshSampleData")
    JsonR refreshSampleData();

    @PostMapping("getSampleData")
    JsonR getSampleData(@Valid MetaGenerator metaGenerator, @RequestParam(defaultValue = "10") int sampleSize);

    @PostMapping("getSampleDataTable")
    JsonR getSampleDataTable(@RequestParam String metaGeneratorJsonArr, @RequestParam(defaultValue = "10") int sampleSize);

    @PostMapping("downloadSampleDataTable")
    byte[] downloadSampleDataTable(@RequestParam String metaGeneratorJsonArr
            , @RequestParam(defaultValue = "10") int sampleSize
            , @RequestParam(defaultValue = "csv") String dataFormat
            , @RequestParam(defaultValue = "data") String tableName
            , @RequestParam(defaultValue = "columnNames") String columnNames);

}