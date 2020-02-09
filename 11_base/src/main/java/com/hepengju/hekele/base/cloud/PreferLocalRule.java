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
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

/**
 * 优先调用本机服务 (顺带区分浏览器调用还是Feign调用)
 *
 * @author hepengju
 */
@Component @Scope("prototype") @Slf4j
public class PreferLocalRule extends RoundRobinRule {

    private static final List<String> LOCAL_IP_LIST = Arrays.asList("localhost", "127.0.0.1", "0:0:0:0:0:0:0:1");

    @Value("${hekele.ribbon.prefer.ip:}")
    private String perferId               ; // 优先访问公共服务
    private String remoteAddr         = ""; // 请求IP地址
    private String requestDescription = ""; // 请求描述（内部调用/外部请求）

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
            if (server.getHost().contains(perferId) && server.isAlive() && server.isReadyToServe()) {
                log.info("{}, 获取服务器列表:{}, 优先访问公共的服务地址:{}", requestDescription, servers, server.getId());
                return server;
            }
        }

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
                try {remoteAddr = InetAddress.getLocalHost().getHostAddress(); } catch (Exception e) {}
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
