package com.hepengju.hekele.data.web;

import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.data.client.DataFeignClient;
import com.hepengju.hekele.data.param.GeneratorParam;
import com.hepengju.hekele.data.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @see DataFeignClient
 */
@Api(tags = "生成数据")
@RestController
@RequestMapping("/data/")
public class GeneratorController {

    @Autowired private GeneratorService genService;

    @ApiOperation("获取生成器分类列表")
    @GetMapping("getGenMap")
    public JsonR getGenMap(){ return JsonR.ok().addData(genService.getGenMap()); }

    @ApiOperation("生成器重新获取数据")
    @PostMapping("fetchDataByGenMeta")
    public JsonR fetchDataByGenMeta(@RequestBody GeneratorParam param) {
        return JsonR.ok().addData(genService.fetchDataByGenMeta(param));
    }

    @ApiOperation("下载定制化的样例数据表格")
    @PostMapping("downloadByGenMeta")
    public void downloadByGenMeta(@RequestBody GeneratorParam param) {
        genService.downloadByGenMeta(param);
    }

}
