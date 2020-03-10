package com.hepengju.hekele.base.util;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Date;

class DiffUtilTest {

    @Test void toJson(){
        Person p1 = new Person("何鹏举",30, 200, new Date(), new Pet("傻狗", 3));
        System.out.println(JSON.toJSONString(p1));
        System.out.println(DiffUtil.getFieldInfoList(p1));
    }


    @Data @AllArgsConstructor @NoArgsConstructor
    static class Person implements Serializable {
        private static final long serialVersionUID = 1L;

        private String name;
        private int age;
        private double salary;
        private Date birth;

        private Pet pet;
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    static class Pet {
        private String name;
        private int age;
    }
}