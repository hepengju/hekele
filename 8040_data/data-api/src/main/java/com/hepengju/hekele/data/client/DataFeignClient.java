package com.hepengju.hekele.data.client;

import com.hepengju.hekele.base.cloud.FeignClientConst;
import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.data.meta.GeneratorMeta;
import com.hepengju.hekele.data.param.GeneratorParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = FeignClientConst.DATA_SERVICE, path = "/data", fallbackFactory = DataFeignClientFallbackFactory.class)
public interface DataFeignClient {

    @GetMapping("getGenMap")
    JsonR<Map<String, List<GeneratorMeta>>> getGenMap();

    @GetMapping("getData")
    JsonR<List<String>> getData(@RequestParam String name, @RequestParam(defaultValue = "5") int sampleSize);

    @PostMapping("refreshTable")
    JsonR<List<Map<String,String>>> refreshTable(@RequestBody GeneratorParam param);

    @PostMapping("downTable")
    byte[] downTable(@RequestBody GeneratorParam param);
}