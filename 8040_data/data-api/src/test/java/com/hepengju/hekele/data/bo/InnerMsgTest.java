package com.hepengju.hekele.data.bo;

import com.hepengju.hekele.base.util.PrintUtil;
import com.hepengju.hekele.data.util.GeneratorUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

public class InnerMsgTest {

    @Test void testGeneratorData(){
        String tableName = GeneratorUtil.getTableName(InnerMsg.class);
        List<String> columnNameList = GeneratorUtil.getColumnNameList(InnerMsg.class);
        List<List<String>> dataList = GeneratorUtil.getDataStringList(InnerMsg.class, 99);
        String printInsert = PrintUtil.printInsert(tableName, columnNameList, dataList, false);
        System.out.println(printInsert);
    }
}
