package com.hepengju.hekele.data.controller;

import com.alibaba.fastjson.JSON;
import com.hepengju.hekele.base.core.R;
import com.hepengju.hekele.base.util.PrintUtil;
import com.hepengju.hekele.data.generator.Generator;
import com.hepengju.hekele.data.meta.MetaGenerator;
import com.hepengju.hekele.data.util.GeneratorUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "生成数据")
@RestController
@RequestMapping("/admin/generator/")
public class GeneratorController {

    /**
     * 生成数据
     * @param classFullName  全类名
     * @param count          个数
     * @param dataFormat     格式(csv, tsv, insert, excel)
     * @return
     */
    @ApiOperation("根据全类名生成数据")
    @GetMapping("/getDataByClassFullName")
    public R getDataByClassFullName(@RequestParam(defaultValue = "com.hepengju.hekele.demo.Person") String classFullName,
                     @RequestParam(defaultValue = "10") Integer count,
                     @RequestParam(defaultValue = "csv") String dataFormat) throws ClassNotFoundException {
        List<List<Object>> dataList = GeneratorUtil.getDataList(Class.forName(classFullName), count);
        String result = PrintUtil.printCSV(dataList);
        return R.ok().addData(result);
    }

    @ApiOperation("根据元数据生成数据")
    @GetMapping("/getDataByMetaJson")
    public R getDataByMetaJson(@RequestParam String metaJson,
                     @RequestParam(defaultValue = "100") Integer count,
                     @RequestParam(defaultValue = "csv") String dataFormat) throws ClassNotFoundException {
        List<MetaGenerator> metaGeneratorList = JSON.parseArray(metaJson, MetaGenerator.class);
        List<Generator> generatorList = metaGeneratorList.stream().map(MetaGenerator::toGenerator).collect(Collectors.toList());
        List<List<Object>> dataList = GeneratorUtil.getDataList(generatorList, count);
        String result = PrintUtil.printCSV(dataList);
        return R.ok().addData(result);
    }


}
