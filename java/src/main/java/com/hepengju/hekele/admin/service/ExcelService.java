package com.hepengju.hekele.admin.service;

import com.hepengju.hekele.base.constant.HeConst;
import com.hepengju.hekele.base.core.Now;
import com.hepengju.hekele.base.core.excel.ExcelBookConfig;
import com.hepengju.hekele.base.core.excel.ExcelSheetCheck;
import com.hepengju.hekele.base.core.excel.ExcelSheetConfig;
import com.hepengju.hekele.base.core.exception.ExcelCheckException;
import com.hepengju.hekele.base.core.exception.HeException;
import com.hepengju.hekele.base.util.ExcelUtil;
import com.hepengju.hekele.base.util.StringUtil;
import com.hepengju.hekele.base.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Service @Slf4j
public class ExcelService {

    private final Integer BATCH_SIZE = 500;
    @Autowired private DbService dbService;

    /**
     * 验证Excel文件并保持下
     */
    public String validExcelAndSave(MultipartFile multipartFile) {
        //文件非空判断和扩展名判断
        if (multipartFile == null || multipartFile.getSize() == 0) throw new HeException("file.uploadNonNull");

        String originalFilename = multipartFile.getOriginalFilename();
        String extName = FilenameUtils.getExtension(originalFilename);
        if (!HeConst.EXCEL_EXT_NAME_LIST.contains(extName.toLowerCase())) throw new HeException("excel.extNameWrong");

        //将上传文件保存到服务器
        //File file = fileService.upload(multipartFile, "excelImport");
        //return file;
        return originalFilename;
    }

    /**
     * 验证Excel配置信息
     */
    private ExcelBookConfig validExcelConfig(String configBeanId) {
        try {
            ExcelBookConfig bookConfig = (ExcelBookConfig) WebUtil.getBean(configBeanId);
            List<ExcelSheetConfig> sheetList = bookConfig.getSheetList();

            if (sheetList.size() == 0) throw new HeException("excel.configException");
            sheetList.forEach(s -> {
                if (StringUtils.isAnyBlank(s.getSheetName(), s.getInsertSql()) || s.getColumnCount() == null) {
                    throw new HeException("excel.configException");
                }
            });

            return bookConfig;
        } catch (Exception e) {
            throw new HeException("excel.configException");
        }
    }

    /**
     * 通用的Excel文件导入
     */
    public void importByConfigBeanId(MultipartFile multipartFile, String configBeanId) {
        String fileName = validExcelAndSave(multipartFile);
        ExcelBookConfig config = validExcelConfig(configBeanId);
        log.info("import excel file begin: {}, fileSize: {}", fileName, FileUtils.byteCountToDisplaySize(multipartFile.getSize()));

        Properties prop = new Properties();


        try (Workbook workbook = WorkbookFactory.create(multipartFile.getInputStream());
             Connection conn = dbService.getConnection()) {
            conn.setAutoCommit(false);

            dbService.execSql(conn, config.getBeforeSqls(), prop);

            // 1.先对每个工作表进行基本验证
            config.getSheetList().forEach(sc -> this.validOneSheet(workbook, sc));

            // 2.每个工作表插入前在进行详细验证
            for (ExcelSheetConfig sheetConfig : config.getSheetList()) {
                dbService.execSql(conn, sheetConfig.getBeforeSqls(), prop);
                this.insertOneSheet(workbook, conn, sheetConfig, multipartFile.getOriginalFilename(), prop);
                dbService.execSql(conn, sheetConfig.getAfterSqls(), prop);
            }

            dbService.execSql(conn, config.getAfterSqls(), prop);
            conn.commit();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        log.info("import excel file finish: {}", fileName);
    }


    /**
     * 验证Sheet存在及标题是否与配置的符合
     */
    private void validOneSheet(Workbook workbook, ExcelSheetConfig sc) {
        String sheetName = sc.getSheetName();
        String columnTitle = sc.getColumnTitle();
        boolean sheetNameMustExist = sc.isSheetNameMustExist();

        // 验证Sheet的存在
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            if (sheetNameMustExist) throw new HeException("excel.sheetNameMustExist", sheetName);
            return;
        }

        // 验证标题
        List<String> titleList = ExcelUtil.getTitleList(sheet, sc.getColumnCount());
        String realTitle = String.join(",", titleList);
        if (StringUtils.isNotBlank(columnTitle) && !columnTitle.equals(realTitle)) {
            throw new HeException("excel.columnTitle.wrong", sheetName);
        }
    }

    /**
     * 插入Sheet数据
     */
    private void insertOneSheet(Workbook workbook, Connection conn, ExcelSheetConfig sc, String fileName, Properties prop) {

        String  sheetName               = sc.getSheetName();
        Integer columnCount             = sc.getColumnCount();
        Integer dataBeginRow            = sc.getDataBeginRow();
        String  insertSql               = sc.getInsertSql();
        boolean sheetNameDataMustExist  = sc.isSheetNameDataMustExist();
        ExcelSheetCheck excelSheetCheck = sc.getExcelSheetCheck();


        // 2. 标题数据和内容数据
        Sheet sheet = workbook.getSheet(sheetName);
        log.info("sheetName : " + sheetName);

        List<String> titleList = ExcelUtil.getTitleList(sheet);
        List<List<Object>> dataList = ExcelUtil.getDataList(sheet,columnCount,dataBeginRow);
        if (dataList.size() == 0) {
            if (sheetNameDataMustExist) throw new HeException("excel.sheetNameDataMustExist", sheetName);
            return;
        }

        // 配置的校验
        if(excelSheetCheck != null) {
            log.info("check begin: " + excelSheetCheck.getClass());
            Map<String,Integer> titleIndexMap = this.getTitleMap(titleList);
            List<String> errList = excelSheetCheck.checkResult(sheetName, columnCount, dataBeginRow, titleIndexMap, dataList);
            if(errList != null && errList.size() > 0) throw new ExcelCheckException(errList);
            log.info("check over");
        }


        //3.批量插入
        int readyCount = 0;

        // 加入一个当前用户的变量
        insertSql = StringUtil.replaceVar(insertSql, prop);

        // 20190508: 特殊字段
        String sql = insertSql.toLowerCase();
        boolean hasTime     = sql.contains("_create_time");
        boolean hasUserCode = sql.contains("_create_user_code");
        boolean hasUserName = sql.contains("_create_user_name");
        boolean hasFile     = sql.contains("_import_file_name");
        boolean hasErr      = sql.contains("_check_error_info");

        try(PreparedStatement pstmt = conn.prepareStatement(insertSql)){
            for (int i = 0; i < dataList.size(); i++) {
                List<Object> rowData = dataList.get(i);
                int colCount = columnCount;

                for (int j = 0; j < columnCount; j++) {
                    Object value = rowData.get(j);

                    //日期类型特殊处理  20200111 hepengju 当时为什么要特殊处理呢? 先注释掉吧
                    //if (value instanceof Date) {
                    //    value = new SimpleDateFormat("yyyyMMdd").format(value);
                    //}

                    // 20190521 空白处理
                    if (value != null && StringUtils.isBlank(value.toString())) {
                        value = null;
                    }
                    pstmt.setObject(j + 1, value);
                }

                // 特殊字段
                if (hasTime)     pstmt.setObject(++colCount, new Date());
                if (hasUserCode) pstmt.setString(++colCount, Now.userCode());
                if (hasUserName) pstmt.setString(++colCount, Now.userName());
                if (hasFile)     pstmt.setString(++colCount, fileName);
                if (hasErr)      pstmt.setString(++colCount, null);

                readyCount++;
                pstmt.addBatch();

                // 批次提交
                if (readyCount % BATCH_SIZE == 0) {
                    pstmt.executeBatch();
                    conn.commit();
                    log.info("ready count: " + readyCount);
                }
            }

            // 整除后还有多余的几条则再次进行提交
            if (readyCount % BATCH_SIZE != 0) {
                pstmt.executeBatch();
                conn.commit();
                log.info("ready count: " + readyCount);
            }

        } catch (SQLException e) {
            throw new HeException(e);
        }
    }

    private static Map<String,Integer> getTitleMap(List<String> titleList){
        Map<String,Integer> map = new LinkedHashMap<>();
        for (int i = 0 ; i < titleList.size() ; i++){
            map.put(titleList.get(i), i);
        }
        return map;
    }
}
