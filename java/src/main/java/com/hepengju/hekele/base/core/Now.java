package com.hepengju.hekele.base.core;

import com.hepengju.hekele.admin.bo.User;
import com.hepengju.hekele.base.util.DateUtil;

import java.util.Date;

/**
 * 取得当前的用户
 *
 * @author hepengju
 */
public class Now {

    private static User user() {
        // TODO 安全集成
        User user = new User();
        user.setUserCode("admin");
        user.setUserName("管理员");
        return user;
    }

    // 当前用户
    public static String userId()   { return user().getUserId(); }
    public static String userCode() { return user().getUserCode(); }
    public static String userName() { return user().getUserName(); }

    // 当前时间
    public static Date date() { return new Date(); }
    public static String yyyyMMddHHmmss() { return DateUtil.yyyMMddHHmmss();}
    public static String yyyyMMdd() { return DateUtil.yyyyMMdd();}

}
