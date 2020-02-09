//package com.hepengju.hekele.base.util;
//
//import com.hepengju.hekele.admin.bo.Role;
//import org.junit.jupiter.api.Test;
//
///**
// * 验证工具测试类
// */
//class ValidUtilTest {
//
//    @Test
//    void validateBean() {
//        Role role = new Role();
//
//        role.setRoleCode("admin");
//        role.setRoleName("管理员");
//        role.setRoleType("N");
//
//        // 角色的角色代码: 不能为空
//        // role.setRoleCode("");
//
//        // 角色的角色名称: 长度需要在0和64之间
//        // role.setRoleName("哈哈哈哈哈哈哈哈哈哈 哈哈哈哈哈哈哈哈哈哈 哈哈哈哈哈哈哈哈哈哈 哈哈哈哈哈哈哈哈哈哈 哈哈哈哈哈哈哈哈哈哈 哈哈哈哈哈哈哈哈哈哈");
//
//        // 角色的角色类型(N-正常, P-职位): 长度需要在1和1之间
//        // role.setRoleType("YY");
//
//        ValidUtil.validateBean(role);
//    }
//
//    @Test
//    void validateProperty() {
//        Role role = new Role();
//        role.setRoleName("管理员");
//
//        // 角色的角色名称: 不能为空
//        // role.setRoleName("");
//        ValidUtil.validateProperty(role, "roleName");
//    }
//}