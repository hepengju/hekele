package com.hepengju.hekele.admin.web;

import com.hepengju.hekele.admin.service.SeqService;
import com.hepengju.hekele.base.core.JsonR;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "序列管理")
@RestController
@RequestMapping("/admin/seq")
public class SeqController {

    @Autowired private SeqService seqService;

    @ApiOperation("指定序列的下一个值")
    @GetMapping("nextval")
    public JsonR nextval(String seqName) { return JsonR.ok().addData(seqService.nextval(seqName)); }

    @ApiOperation("指定序列当日的下一个值")
    @GetMapping("nextvalToday")
    public JsonR nextvalToday(String seqName) { return JsonR.ok().addData(seqService.nextvalToday(seqName)); }

    @ApiOperation("指定序列的下一个格式化的值")
    @GetMapping("nextvalFormat")
    public JsonR nextvalFormat(String seqName, int formatLength) { return JsonR.ok().addData(seqService.nextvalFormat(seqName, formatLength)); }

    @ApiOperation("指定序列当日的下一个格式化的值")
    @GetMapping("nextvalFormatToday")
    public JsonR nextvalFormatToday(String seqName, int formatLength) { return JsonR.ok().addData(seqService.nextvalFormatToday(seqName, formatLength)); }


}
