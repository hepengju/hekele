package com.hepengju.hekele.base.aspect;

import com.alibaba.fastjson.JSON;
import com.hepengju.hekele.base.core.Now;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.hepengju.hekele.base.util.WebUtil.getHttpServletRequest;
import static com.hepengju.hekele.base.util.WebUtil.getIpAddr;

/**
 * Web层的日志切面类
 *
 * <pre>
 *     日期: 20191211
 *     作者: 何鹏举
 * </pre>
 *
 * @see com.hepengju.hekele.base.annotation.WebLogNull
 * @see com.hepengju.hekele.base.annotation.WebLogDB
 */
@Component @Aspect @Slf4j
public class WebLogAspect {

    // 切点: Controller的所有方法, 排除WebLogNull注解的方法
    @Pointcut("(  execution(* com.hepengju.hekele..*Controller.*(..))  " +
            " && !@annotation(com.hepengju.hekele.base.annotation.WebLogNull) )  ")
    public void logPointCut() {
    }

    // 环绕通知: 整个方法try..catch..起来, 不允许抛出异常
    @Around("logPointCut()")
    @Order(0)
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime beginTime = LocalDateTime.now();

        LogBefore lb = new LogBefore();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName();
        Object[] args = joinPoint.getArgs();
        lb.setMethodName(methodName);
        lb.setMethodParam(Arrays.toString(args));

        try {lb.setUserCode(Now.userCode()); } catch (Exception e) {}
        HttpServletRequest request = getHttpServletRequest();
        if (request != null) {
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
    public static class LogBefore {
        private String userCode;
        private String ipAddr;
        private String requestUri;
        private String methodName;
        private String methodParam;

        @Override
        public String toString() {
            return "==> 用户: " + userCode
                    + ", IP地址: " + ipAddr
                    + ", 请求URI: " + requestUri
                    + ", 方法名: " + methodName
                    + ", 方法参数: " + methodParam;
        }
    }

    @Data
    public static class LogAfter {
        private String methodCost;
        private boolean throwException;
        private String methodReturn;

        @Override
        public String toString() {
            return "<== 耗时: " + methodCost
                    + ", 抛出异常: " + throwException
                    + ", 返回值: " + methodReturn;
        }
    }
}
