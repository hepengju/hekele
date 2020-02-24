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

    private String sql = "SELECT USER_ID AS 主键, USER_NAME AS 姓名, GENDER AS 性别, PHONE AS 手机号, BIRTH AS 出生日期, FAMILY_POPULATION AS 家庭人口, SALARY_YEAR AS 年收入, MANAGER_NO AS 经理编号, ORG_NO AS 机构编号, FAMILY_ADDR AS 家庭地址 FROM PERSON LIMIT 10";
    private String fileName = "/Users/hepengju/person.xlsx";

    @Test
    void exportFromDB() throws SQLException, FileNotFoundException {
        Connection conn = DriverManager.getConnection(url, username, password);
        ExcelUtil.exportFromDB(conn, sql, new FileOutputStream(fileName));
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

        FileOutputStream os1 = new FileOutputStream(new File("/Users/hepengju/exportFromList1.xlsx"));
        ExcelUtil.exportFromList(title, data, os1);

        FileOutputStream os2 = new FileOutputStream(new File("/Users/hepengju/exportFromList2.xlsx"));
        ExcelUtil.exportFromList(title, data, os2, "姓名簿");


        Map<String, List<String>> titleMap = new LinkedHashMap<>();
        Map<String, List<List<Object>>> dataMap = new LinkedHashMap<>();

        // 标题
        List<String> title02 = new ArrayList<>();
        title02.add("姓名");
        title02.add("年龄");

        // 数据
        List<List<Object>> data02 = new ArrayList<>();
        List<Object> p3 = new ArrayList<>();
        p3.add("孙悟空");
        p3.add(18);
        List<Object> p4 = new ArrayList<>();
        p4.add("猪八戒");
        p4.add(28);
        data02.add(p1);
        data02.add(p2);

        titleMap.put("人员", title);
        dataMap.put("人员", data);
        titleMap.put("人员2", title02);
        dataMap.put("人员2", data02);

        FileOutputStream os3 = new FileOutputStream(new File("/Users/hepengju/exportFromList3.xlsx"));
        ExcelUtil.exportFromList(titleMap, dataMap, os3);
    }

    @Test
    void readSheetNameList() {
        List<String> sheetNameList = ExcelUtil.readSheetNameList(fileName);
        System.out.println("sheetNameList = " + sheetNameList);
    }

    @Test
    void readTitle() {
        Map<String, List<String>> titleListMap = ExcelUtil.readTitle(fileName);
        System.out.println("titleListMap = " + titleListMap);
    }

    @Test
    void readTitleFirstSheet() {
        List<String> title = ExcelUtil.readTitleFirstSheet(fileName);
        System.out.println("title = " + title);
    }

    @Test
    void readTitleBySheetName() {
        List<String> title = ExcelUtil.readTitleBySheetName(fileName,"人员表2");
        System.out.println("title = " + title);
    }

    @Test
    void readTitleBySheetNameAndLastColumn() {
        List<String> title = ExcelUtil.readTitleBySheetNameAndLastColumn(fileName,"人员表2", 9);
        System.out.println("title = " + title);
    }

    @Test
    void readData() {
        Map<String, List<List<String>>> dataListMap = ExcelUtil.readData(fileName);
        dataListMap.forEach((k,v) -> {
            System.out.println(k + ": " + v);
        });
    }

    @Test
    void readDataFirstSheet() {
        List<List<String>> data = ExcelUtil.readDataFirstSheet(fileName);
        System.out.println("data = " + data);
    }

    @Test
    void readDataBySheetName() {
        List<List<String>> data = ExcelUtil.readDataBySheetName(fileName,"人员表3");
        System.out.println("data = " + data);
    }

    @Test
    void readDataFirstSheetAndOtherLimit() {
        List<List<String>> data = ExcelUtil.readDataFirstSheetAndOtherLimit(fileName,1,2,5);
        System.out.println("data = " + data);
    }

    @Test
    void readDataBySheetNameAndOtherLimit() {
        List<List<String>> data = ExcelUtil.readDataFirstSheetAndOtherLimit(fileName,1,2,5);
        System.out.println("data = " + data);
    }

}