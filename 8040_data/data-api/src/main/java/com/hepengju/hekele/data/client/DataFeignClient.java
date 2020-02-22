package com.hepengju.hekele.data.client;

import com.hepengju.hekele.base.cloud.FeignClientConst;
import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.data.meta.GeneratorMeta;
import com.hepengju.hekele.data.param.GeneratorParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @see com.hepengju.hekele.data.web.GeneratorController
 */
@FeignClient(value = FeignClientConst.DATA_SERVICE, path = "/data", fallbackFactory = DataFeignClientFallbackFactory.class)
public interface DataFeignClient {

    @GetMapping("getGenMap")
    JsonR<Map<String, List<GeneratorMeta>>> getGenMap();

    @PostMapping("fetchDataByGenMeta")
    JsonR<List<Map<String,String>>> fetchDataByGenMeta(@RequestBody GeneratorParam param);

    @PostMapping("downloadByGenMeta")
    byte[] downloadByGenMeta(@RequestBody GeneratorParam param);
}