package com.hepengju.hekele.base.util;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DiffUtilTest {


    @Test void testMap(){
        Map<String,String> map = new HashMap<>();
        map.put("name","何鹏举");
        map.put("age","30");
        System.out.println(JSON.toJSONString(map));
        System.out.println(DiffUtil.getFieldInfoList(map));
    }

    @Test void testBase(){
        System.out.println(DiffUtil.getFieldInfoList(10));
    }

    @Test void testGet(){
        Person p1 = new Person("何鹏举",30, 200.0, new Date(),null, new Pet("傻狗", 3));
        Person p2 = new Person("孙悟空",18, 200, null, LocalDate.now(), new Pet("傻狗", 10));
        List<DiffUtil.DiffInfo> infoList = DiffUtil.get(p1, p2);

        infoList.forEach(System.out::println);
    }


    @Data @AllArgsConstructor @NoArgsConstructor
    static class Person implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private int age;
        private double salary;
        private Date birth;

        private LocalDate localDate;
        private Pet pet;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    static class Pet {
        private String name;
        private int age;
    }
}