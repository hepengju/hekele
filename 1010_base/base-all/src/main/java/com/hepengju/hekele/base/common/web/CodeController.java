package com.hepengju.hekele.base.common.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hepengju.hekele.base.common.bo.Code;
import com.hepengju.hekele.base.common.service.CodeService;
import com.hepengju.hekele.base.core.JsonR;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "代码管理")
@RestController
@RequestMapping("/admin/code")
public class CodeController {

    @Autowired private CodeService codeService;

    @ApiOperation("查询所有")
    @GetMapping("list")
    public JsonR<Code> list(Long pageNum, Long pageSize, Code code) {
        LambdaQueryWrapper<Code> wrapper = new LambdaQueryWrapper<>();
        wrapper .eq(StringUtils.isNotBlank(code.getTypeCode()),Code::getTypeCode, code.getTypeCode())
                .like(StringUtils.isNotBlank(code.getTypeName()),Code::getTypeName, code.getTypeName())
                .eq(StringUtils.isNotBlank(code.getItemCode()),Code::getItemCode, code.getItemCode())
                .like(StringUtils.isNotBlank(code.getItemName()),Code::getItemName, code.getItemName())
                .orderByAsc(Code::getSysCode, Code::getTypeCode, Code::getItemSort);
        return codeService.listR(pageNum, pageSize, wrapper);
    }

    @ApiOperation("根据主键查询")
    @GetMapping("getById")
    public JsonR<Code> getById(@RequestParam String codeId) { return codeService.getRById(codeId);}

    @ApiOperation("根据多个类型代码查询, 返回代码值List")
    @GetMapping("getByTypeCodes")
    public JsonR<Code> getByTypeCodes(@RequestParam("typeCode") String[] typeCodes) {
        return JsonR.ok().addData(codeService.getByTypeCodes(typeCodes));
    }

    @ApiOperation("根据多个类型代码查询, 返回代码值Map")
    @GetMapping("getMapByTypeCodes")
    public JsonR getMapByTypeCodes(@RequestParam("typeCode") String[] typeCodes) {
        return JsonR.ok().addData(codeService.getMapByTypeCodes(typeCodes));
    }

    @ApiOperation("新增代码")
    @PostMapping("add")
    public JsonR add(@Valid Code code) {
        code.setCodeId(null);
        validCode(code);
        codeService.addR(code);
        codeService.refresh();
        return JsonR.ok();
    }

    @ApiOperation("编辑代码")
    @PostMapping("edit")
    public JsonR edit(@Valid Code code) {
        validCode(code);
        codeService.editR(code);
        codeService.refresh();
        return JsonR.ok();
    }

    // 20190705 何鹏举 前端添加代码值的时候, 直接复制, 两边有空格, 需要trim处理(针对关键字段)
    private void validCode(Code code) {
        code.setTypeCode(StringUtils.trim(code.getTypeCode()));
        code.setTypeName(StringUtils.trim(code.getTypeName()));
        code.setItemCode(StringUtils.trim(code.getItemCode()));
        code.setItemName(StringUtils.trim(code.getItemName()));

        codeService.validUnique("type_code", code.getTypeCode()
                              , "item_code", code.getItemCode()
                              , code.getCodeId(), "code.typeCodeItemCode.exist", code.getTypeCode(), code.getItemCode());
    }

    @ApiOperation("删除代码")
    @PostMapping("delete")
    public JsonR delete(@RequestParam("codeId") List<String> idList) {
        codeService.deleteR(idList);
        codeService.refresh();
        return JsonR.ok();
    }

    @ApiOperation("启用代码")
    @PostMapping("enable")
    public JsonR enable(@RequestParam("codeId") List<String> idList) {
        codeService.enableBatchByIds(idList);
        codeService.refresh();
        return JsonR.ok();
    }

    @ApiOperation("禁用代码")
    @PostMapping("disable")
    public JsonR disable(@RequestParam("codeId") List<String> idList) {
        codeService.disableBatchByIds(idList);
        codeService.refresh();
        return JsonR.ok();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @ApiOperation("清空并重新插入所有代码-开发环境使用")
    @PostMapping("truncateThenInsertAll")
    public JsonR truncateThenInsertAll(@RequestBody List<Code> codeList) {
        return codeService.truncateThenInsertAll(codeList);
    }

}
