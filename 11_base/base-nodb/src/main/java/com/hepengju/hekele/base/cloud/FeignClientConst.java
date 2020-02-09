package com.hepengju.hekele.base.cloud;

/**
 * Feign远程调用的服务常量
 *
 * @author hepengju
 */
public interface FeignClientConst {

    String SPRING_EUREKA  = "spring-eureka"  ; // 注册中心
    String SPRING_CONFIG  = "spring-config"  ; // 配置中心
    String SPRING_MONITOR = "spring-monitor" ; // 监控服务

    String ADMIN_SERVICE   = "admin-service"   ; // 人员角色菜单
    String FILE_SERVICE    = "file-service"    ; // 文件管理
    String MESSAGE_SERVICE = "message-service" ; // 消息中共新
    String DATA_SERVICE    = "data-service"    ; // 模拟数据

}
