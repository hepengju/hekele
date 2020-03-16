package com.hepengju.hekele.base.core;

import com.alibaba.fastjson.JSON;
import com.hepengju.hekele.base.util.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * 取得当前的用户
 *
 * @author hepengju
 */
public class Now {

    public static String NOW_USER_FEIGN = "NOW-USER-FEIGN";
    public static String NOW_USER_ZUUL  = "NOW-USER-ZUUL";

    public static NowUser user() {
        NowUser user = new NowUser();
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

    @Data
    public static class NowUser{
        private String userId;
        private String userCode;
        private String userName;

        public String toString(){
            return JSON.toJSONString(this);
        }
    }
}
