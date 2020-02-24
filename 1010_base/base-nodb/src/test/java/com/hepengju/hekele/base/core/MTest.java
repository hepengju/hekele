package com.hepengju.hekele.base.core;

import org.junit.jupiter.api.Test;

class MTest {
    @Test
    void testGet() {
        System.out.println(M.get("no_login")); // 要能正常获取
        System.out.println(M.get("login.accountUnlockInMinutes", 10));     // 参数要能设置进去
        System.out.println(M.get("login.accountUnlockInMinutes", "aa"));   // 参数类型错误, 不能报错
        System.out.println(M.get("login.accountUnlockInMinutes", 10, 20)); // 参数多不能报错
    }

    @Test void testPrintCSVFormat() {
        M.printCSVFormat();
    }
}