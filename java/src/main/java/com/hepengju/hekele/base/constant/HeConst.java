package com.hepengju.hekele.base.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 常量类
 */
public interface HeConst {

    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String DATE_FORMAT      = "yyyy-MM-dd";

    List<String> EXCEL_EXT_NAME_LIST = Arrays.asList("xls", "xlsx", "xlsm");

    /**
     * 内置返回代码
     *
     * @see com.hepengju.hekele.base.core.M
     */
    interface MCode {
        int SUCCESS                     = 0;
        int UNKNOWN_ERROR               = -1;
        int NO_LOGIN                    = 10000;
        int SPRINGMVC_FORM_BIND_ERROR   = 10001;
        int SPRINGMVC_JSON_BIND_ERROR   = 10002;
        int HIBERNATE_BEAN_VALID_ERROR  = 10003;
        int MISSING_PARAMETER_EXCEPTION = 10004;
    }
}
