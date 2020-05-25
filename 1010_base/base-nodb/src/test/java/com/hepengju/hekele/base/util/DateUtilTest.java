package com.hepengju.hekele.base.util;

import org.junit.jupiter.api.Test;

import java.util.Date;

class DateUtilTest {

    @Test
    public void testDate(){
        Date date01 = DateUtil.stringToDate("2020-03-20 12:20:30");
        Date date02 = DateUtil.stringToDate("2020-3-20 12:20:30");
        Date date03 = DateUtil.stringToDate("2020-3-2");
        System.out.println(date01);
        System.out.println(date02);
        System.out.println(date03);
    }

}