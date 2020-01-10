package com.hepengju.hekele.base.util;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

class ExcelUtilTest {

    private String url = "jdbc:mysql://mysql.hepengju.com:3306/hekele?serverTimezone=Asia/Shanghai";
    private String username = "hekele";
    private String password = "hekele123";

    @Test
    void exportFromDB() throws SQLException, FileNotFoundException {
        Connection conn = DriverManager.getConnection(url, username, password);
        ExcelUtil.exportFromDB(conn, "select * from person", new FileOutputStream(new File("d:/person.xlsx")));
    }

    @Test
    void exportFromList() throws FileNotFoundException {
        // 标题
        List<String> title = new ArrayList<>();
        title.add("姓名");
        title.add("年龄");

        // 数据
        List<List<Object>> data = new ArrayList<>();
        List<Object> p1 = new ArrayList<>();
        p1.add("孙悟空");
        p1.add(18);
        List<Object> p2 = new ArrayList<>();
        p2.add("猪八戒");
        p2.add(28);
        data.add(p1);
        data.add(p2);

        Map<String, List<String>> titleMap = new LinkedHashMap<>();
        Map<String, List<List<Object>>> dataMap = new LinkedHashMap<>();



        FileOutputStream os1 = new FileOutputStream(new File("d:/exportFromList1.xlsx"));
        ExcelUtil.exportFromList(title, data, os1);

        FileOutputStream os2 = new FileOutputStream(new File("d:/exportFromList2.xlsx"));
        ExcelUtil.exportFromList(title, data, os2, "姓名簿");

        FileOutputStream os3 = new FileOutputStream(new File("d:/exportFromList3.xlsx"));
        ExcelUtil.exportFromList(titleMap, dataMap, os3);
    }

    @Test
    void testExportFromList() {
    }

    @Test
    void readSheetNameList() {
    }

    @Test
    void readTitle() {
    }

    @Test
    void readTitleFirstSheet() {
    }

    @Test
    void readTitleBySheetName() {
    }

    @Test
    void readTitleBySheetNameAndLastColumn() {
    }

    @Test
    void readData() {
    }

    @Test
    void readDataFirstSheet() {
    }

    @Test
    void readDataBySheetName() {
    }

    @Test
    void readDataFirstSheetAndOtherLimit() {
    }

    @Test
    void readDataBySheetNameAndOtherLimit() {
    }
}