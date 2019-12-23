package com.hepengju.hekele.base.core;

import com.hepengju.hekele.admin.bo.User;

/**
 * 取得当前的用户
 */
public class Now {

    public static User user(){
        // TODO 安全集成
        User user = new User();
        user.setUserCode("admin");
        user.setUserName("管理员");
        return user;
    }

    public static String userCode(){
        return user().getUserCode();
    }
    public static String userName(){
        return user().getUserName();
    }

}
