package com.hepengju.hekele.base.constant;

/**
 * 常量类
 */
public interface HeConst {

    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String DATE_FORMAT      = "yyyy-MM-dd";

    /**
     * 错误代码, 其他的错误代码, 参看 he.message.json
     */
    interface ErrorCode {
        int SUCCESS                    = 0;
        int UNKOWN_ERROR               = -1;
        int NO_LOGIN                   = 10000;
        int SPRINGMVC_FORM_BIND_ERROR  = 10001;
        int SPRINGMVC_JSON_BIND_ERROR  = 10002;
        int HIBERNATE_BEAN_VALID_ERROR = 10003;
    }
}
