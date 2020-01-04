package com.hepengju.hekele.base.aspect;

import com.alibaba.fastjson.JSON;
import com.hepengju.hekele.base.annotation.WebLogNull;
import com.hepengju.hekele.base.core.Now;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Web层的日志切面类
 *
 * <pre>
 *     日期: 20191211
 *     作者: 何鹏举
 *     说明: SCL-687 用户登录操作后，系统需要对其操作进行留存。以便进行追溯
 * </pre>
 *
 * @see com.hepengju.hekele.base.annotation.WebLogNull
 * @see com.hepengju.hekele.base.annotation.WebLogToDB
 *
 */
@Component @Aspect
@Slf4j
public class WebLogAspect {

    // 切入点: Controller的所有public方法
    @Pointcut("(  execution(* com.hepengju.hekele..*Controller.*(..))  " +
            " && !@annotation(com.hepengju.hekele.base.annotation.WebLogNull) )  ")
    public void logPointCut(){}

    // 环绕通知: 整个方法try..catch..起来, 不允许抛出异常
    @Around("logPointCut()") @Order(0)
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime beginTime = LocalDateTime.now();

        LogBefore lb = new LogBefore();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();
        lb.setMethodName(methodName);
        lb.setMethodParam(Arrays.toString(args));

        try { lb.setUserCode(Now.userCode()); } catch (Exception e) {}
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request != null){
            lb.setIpAddr(getIpAddr(request));
            lb.setRequestUri(request.getRequestURI());
        }

        // 记录方法进入前的参数
        log.info(lb.toString());

        LogAfter la = new LogAfter();
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
            la.setThrowException(false);
            la.setMethodReturn(JSON.toJSONString(proceed));
        } catch (Throwable e) {
            la.setThrowException(true);
            throw e;
        } finally {
            LocalDateTime endTime = LocalDateTime.now();
            la.setMethodCost(Duration.between(beginTime, endTime).toMillis() + "ms");

            // 记录方法返回值
            log.info(la.toString());
        }
        return proceed;
    }

    @Data
    public static class LogBefore{
        private String userCode;
        private String ipAddr;
        private String requestUri;
        private String methodName;
        private String methodParam;

        @Override
        public String toString() {
            return "==> 用户: "    + userCode
                    + ", IP地址: "  + ipAddr
                    + ", 请求URI: " + requestUri
                    + ", 方法名: "  + methodName
                    + ", 方法参数: " + methodParam;
        }
    }

    @Data
    public static class LogAfter{
        private String  methodCost;
        private boolean throwException;
        private String  methodReturn;

        @Override
        public String toString() {
            return "<== 耗时: "     + methodCost
                    + ", 是否异常: " + throwException
                    + ", 返回值: "   + methodReturn;
        }
    }

    /**
     * 根据Request获取IP地址
     *
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，
     * 而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        // 代理处理
        String ip = request.getHeader("x-forwarded-for");
        if (blankOrUnknown(ip)) ip = request.getHeader("Proxy-Client-IP");
        if (blankOrUnknown(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
        if (blankOrUnknown(ip)) ip = request.getHeader("HTTP_CLIENT_IP");
        if (blankOrUnknown(ip)) ip = request.getHeader("HTTP_X_FORWARDED_FOR");

        // 多级代理
        if (StringUtils.isNotBlank(ip) && ip.contains(",")) ip = ip.split(",")[0];

        // 正常处理
        if (blankOrUnknown(ip)) ip = request.getRemoteAddr();

        // 特殊设置
        if("0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip)) ip = "127.0.0.1";

        //非空限定
        if(ip == null) ip = "unknown";

        return ip;
    }

    private static boolean blankOrUnknown(String ip) {
        return StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip);
    }
}
