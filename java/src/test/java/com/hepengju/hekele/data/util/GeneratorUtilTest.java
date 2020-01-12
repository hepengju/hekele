package com.hepengju.hekele.data.util;

import com.hepengju.hekele.base.util.PrintUtil;
import com.hepengju.hekele.demo.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

class GeneratorUtilTest {

    @Test
    void getDataList() {
        List<List<Object>> dataList = GeneratorUtil.getDataList(Person.class, 100);
        String result = PrintUtil.printTSV(dataList);
        System.out.println(result);
    }
}