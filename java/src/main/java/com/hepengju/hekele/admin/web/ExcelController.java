package com.hepengju.hekele.admin.web;

import com.hepengju.hekele.admin.service.DbService;
import com.hepengju.hekele.admin.service.ExcelService;
import com.hepengju.hekele.base.core.Now;
import com.hepengju.hekele.base.core.R;
import com.hepengju.hekele.base.util.ExcelUtil;
import com.hepengju.hekele.base.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Excel下载模板, 导入, 导出的通用处理
 *
 * @author he_pe
 */
@Api(tags = "Excel文件操作")
@RestController @Slf4j
@RequestMapping("/admin/excel")
public class ExcelController {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    @Autowired private DbService dbService;
    @Autowired private ExcelService excelService;

    @ApiOperation("根据模板文件名称下载模板")
    @GetMapping("downloadTemplate")
    public void downloadTemplate(String templateFileName) throws IOException {
        templateFileName = StringUtils.isBlank(templateFileName) ? "public/excel/模板-人员表.xlsx" : templateFileName;
        String downloadFileName = FilenameUtils.getName(templateFileName);
        WebUtil.handleFileDownload(downloadFileName,
                resourceLoader.getResource("classpath:" + templateFileName).getInputStream());
    }

    @ApiOperation("数据库SQL语句导出")
    @PostMapping("exportFromDbBySQL")
    public void exportFromDbBySQL(@RequestParam String sql) throws IOException {
        WebUtil.handleFileDownload("sql导出-" + Now.yyyyMMddHHmmss() + ".xlsx");
        ExcelUtil.exportFromDB(dbService.getConnection(), sql, WebUtil.getHttpServletResponse().getOutputStream());
    }

    @ApiOperation("根据配置导入Excel")
    @PostMapping("importByConfigBeanId")
    public R importByConfigBeanId(@RequestParam("file") MultipartFile multipartFile, @RequestParam String configBeanId) throws SQLException, IOException {
        excelService.importByConfigBeanId(multipartFile, configBeanId);
        return R.ok();
    }

}
