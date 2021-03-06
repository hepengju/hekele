package com.hepengju.hekele.base.util;

import com.hepengju.hekele.data.util.GeneratorUtil;
import com.hepengju.hekele.demo.bo.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

class PrintUtilTest {

    @Test
    void printInsert() {
        String tableName = GeneratorUtil.getTableName(Person.class);
        List<String> columnNameList = GeneratorUtil.getColumnTitleList(Person.class);
        List<List<String>> dataList = GeneratorUtil.getDataStringList(Person.class, 8);

        String result01 = PrintUtil.printInsert(tableName, columnNameList, dataList, false);
        String result02 = PrintUtil.printInsert(tableName, columnNameList, dataList, true);
        System.out.println(result01);
        System.out.println(result02);
    }
}