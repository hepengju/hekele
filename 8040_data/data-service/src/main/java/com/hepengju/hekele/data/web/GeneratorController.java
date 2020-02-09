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

    @ApiOperation("获取定制化的样例数据")
    @PostMapping("getSampleData")
    public JsonR getSampleData(@Valid MetaGenerator metaGenerator, @RequestParam(defaultValue = "10") int sampleSize) {
        return JsonR.ok().addData(genService.getSampleDataByMeta(metaGenerator, sampleSize));
    }

    @ApiOperation("获取定制化的样例数据表格")
    @PostMapping("getSampleDataTable")
    public JsonR getSampleDataTable(@RequestParam String metaGeneratorJsonArr, @RequestParam(defaultValue = "10") int sampleSize) {
        return JsonR.ok().addData(genService.getSampleDataTableByMeta(metaGeneratorJsonArr, sampleSize));
    }

    @ApiOperation("下载定制化的样例数据表格")
    @PostMapping("downloadSampleDataTable")
    public void downloadSampleDataTable(@RequestParam String metaGeneratorJsonArr
            , @RequestParam(defaultValue = "10") int sampleSize
            , @RequestParam(defaultValue = "csv") String dataFormat
            , @RequestParam(defaultValue = "data") String tableName
            , @RequestParam(defaultValue = "columnNames") String columnNames) {
        genService.downloadSampleDataTable(metaGeneratorJsonArr, sampleSize, dataFormat, tableName, columnNames);
    }

    // 工程拆分，Person类已经不在此工程中了
//    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    /**
//     * 生成数据
//     * @param classFullName  全类名
//     * @param count          个数
//     * @param dataFormat     格式(csv, tsv, sql, excel)
//     */
//    @ApiOperation("根据全类名生成数据")
//    @GetMapping("/getDataByClassFullName")
//    public JsonR getDataByClassFullName(@RequestParam(defaultValue = "com.hepengju.hekele.demo.Person") String classFullName,
//                     @RequestParam(defaultValue = "10") Integer count,
//                     @RequestParam(defaultValue = "csv") String dataFormat) throws ClassNotFoundException {
//        List<List<Object>> dataList = GeneratorUtil.getDataList(Class.forName(classFullName), count);
//        String result = PrintUtil.printCSV(dataList);
//        return JsonR.ok().addData(result);
//    }
//
//    @ApiOperation("根据全类名下载数据")
//    @GetMapping("/downloadDataByClassFullName")
//    public void downloadDataByClassFullName(@RequestParam(defaultValue = "com.hepengju.hekele.demo.Person") String classFullName,
//                     @RequestParam(defaultValue = "1050") Integer count,
//                     @RequestParam(defaultValue = "csv") String dataFormat) {
//        genService.downloadDataByClassFullName(classFullName, count, dataFormat);
//    }

}
