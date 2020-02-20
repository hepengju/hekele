package com.hepengju.hekele.data.web;

import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.data.meta.MetaGenerator;
import com.hepengju.hekele.data.service.GeneratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "生成数据")
@RestController
@RequestMapping("/data/")
public class GeneratorController {

    @Autowired private GeneratorService genService;

    @ApiOperation("获取生成器列表")
    @GetMapping("getGenList")
    public JsonR getGenList(){ return JsonR.ok().addData(genService.getGenList());}

    @ApiOperation("获取生成器分类列表")
    @GetMapping("getGenMap")
    public JsonR getGenMap(){ return JsonR.ok().addData(genService.getGenMap()); }

    @ApiOperation("刷新所有的样例数据")
    @PostMapping("refreshSampleData")
    public JsonR refreshSampleData() {
        genService.refreshSampleData();
        return JsonR.ok().addData(genService.getGenMap());
    }

    @ApiOperation("单个生成器重新获取数据")
    @PostMapping("getSampleData")
    public JsonR getSampleData(@Valid MetaGenerator metaGenerator, @RequestParam(defaultValue = "10") int sampleSize) {
        return JsonR.ok().addData(genService.getSampleDataByMeta(metaGenerator, sampleSize));
    }

    @ApiOperation("多个生成器重新获取数据")
    @PostMapping("getSampleDataTable")
    public JsonR getSampleDataTable(@RequestParam String metaGeneratorJsonArr, @RequestParam(defaultValue = "10") int sampleSize) {
        return JsonR.ok().addData(genService.getSampleDataTableByMeta(metaGeneratorJsonArr, sampleSize));
    }

    @ApiOperation("下载定制化的样例数据表格")
    @RequestMapping(value = "downloadSampleDataTable", method = {RequestMethod.GET, RequestMethod.POST})
    public void downloadSampleDataTable(
            @RequestParam(defaultValue = "[{className:\"com.hepengju.hekele.data.generator.number.IntegerGenerator\",min:0,max:100},{className:\"com.hepengju.hekele.data.generator.string.CodeGenerator\",code:\"A,B,C,D,E\"}]")
                                                          String metaGeneratorJsonArr
            , @RequestParam(defaultValue = "10")          int    sampleSize
            , @RequestParam(defaultValue = "csv")         String dataFormat
            , @RequestParam(defaultValue = "data")        String tableName
            , @RequestParam(defaultValue = "年龄,代码") String columnNames) {
        genService.downloadSampleDataTable(metaGeneratorJsonArr, sampleSize, dataFormat, tableName, columnNames);
    }

}
