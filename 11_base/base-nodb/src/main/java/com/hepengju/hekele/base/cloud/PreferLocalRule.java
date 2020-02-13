package com.hepengju.hekele.base.cloud;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 优先调用本机服务 (顺带区分前端js经过Zuul代理直接调用还是Feign调用)
 *
 * <pre>
 *     举例: 在demo-service中访问
 *      1. 浏览器直接经过zuul代理访问: http://localhost:8000/data-service/data/getGenList, 日志如下
 *          外部调用:192.168.0.105:8000, 请求URI：/data-service/data/getGenList, 获取服务器列表:[101.132.97.183:8040, 192.168.0.105:8040], 优先访问公共的服务地址:101.132.97.183:8040
 *      2. demo-service内部通过FeignClient进行访问, 日志如下
 *          内部调用:192.168.0.105:8000, 获取服务器列表:[101.132.97.183:8040, 192.168.0.105:8040], 优先访问本机的服务地址:192.168.0.105:8040
 *
 *     定制化是否开启
 *      1. 注释掉 @Component @Scope("prototype")
 *      2. 编写 PreferLocalRuleConfiguration, 添加 @ConditionalOnProperty
 *
 *          @Configuration
 *          @ConditionalOnProperty(name = "hekele.preferLocalRule.enable", havingValue = "true", matchIfMissing = true)
 *          public class PreferLocalRuleConfiguration {
 *
 *              @Bean @Scope("prototype")
 *              public PreferLocalRule preferLocalRule (){
 *                  return new PreferLocalRule();
 *              }
 *          }
 *
 * </pre>
 *
 * @see org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration
 *
 * @author hepengju
 */
@Slf4j @Component @Scope("prototype")
public class PreferLocalRule extends RoundRobinRule {

    private static final List<String> LOCAL_IP_LIST = Arrays.asList("localhost", "127.0.0.1", "0:0:0:0:0:0:0:1");

    @Value("${hekele.ribbon.prefer.ip:}")
    private String preferId;                // 优先访问公共服务
    private String remoteAddr         = ""; // 请求IP地址
    private String requestDescription = ""; // 请求描述（内部调用/外部请求）

    @Value("${eureka.instance.ip-address:}")
    private String eurekaInstanceIpAddress; // 本机的IP地址, 也可 InetAddress.getLocalHost().getHostAddress()

    @Autowired
    private Registration registration;

    /**
     * 核心选择服务
     */
    private Server selectService(List<Server> servers, String remoteAddr) {
        if (CollectionUtils.isEmpty(servers)) return null;

        for (Server server : servers) {
            if (server.getHost().equals(remoteAddr) && server.isAlive() && server.isReadyToServe()) {
                log.info("{}, 获取服务器列表:{}, 优先访问本机的服务地址:{}", requestDescription, servers, server.getId());
                return server;
            }
        }

        for (Server server : servers) {
            if (server.getHost().contains(preferId) && server.isAlive() && server.isReadyToServe()) {
                log.info("{}, 获取服务器列表:{}, 优先访问公共的服务地址:{}", requestDescription, servers, server.getId());
                return server;
            }
        }

        log.info("{}, 获取服务器列表:{}, 偏好本地规则未找到合适服务, 启用默认轮询机制(RoundRobinRule)", requestDescription, servers);
        return null;
    }

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        // zuul的请求
        HttpServletRequest zuulRequest = RequestContext.getCurrentContext().getRequest();
        if (zuulRequest != null) {
            remoteAddr = zuulRequest.getRemoteAddr();
            // 20190902 hepengju 处理localhost请求
            if (LOCAL_IP_LIST.contains(remoteAddr)) {
                //try {remoteAddr = InetAddress.getLocalHost().getHostAddress(); } catch (Exception e) {}
                remoteAddr = eurekaInstanceIpAddress;
            }
            requestDescription = "外部调用:" + remoteAddr + ", 请求URI：" + zuulRequest.getRequestURI();
        } else {
            remoteAddr = registration.getHost();
            requestDescription = "内部调用:" + remoteAddr + ":" + registration.getPort();
        }

        List<Server> reachableServers = lb.getAllServers();
        Server chooseService = selectService(reachableServers, remoteAddr);
        if(chooseService != null) return chooseService;

        return super.choose(lb, key);
    }

}
