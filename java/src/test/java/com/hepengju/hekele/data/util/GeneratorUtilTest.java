package com.hepengju.hekele.data.util;

import com.hepengju.hekele.demo.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

class GeneratorUtilTest {

    @Test
    void getDataList() {
        List<List<Object>> dataList = GeneratorUtil.getDataList(Person.class, 100);
        dataList.forEach(System.out::println);
    }
}