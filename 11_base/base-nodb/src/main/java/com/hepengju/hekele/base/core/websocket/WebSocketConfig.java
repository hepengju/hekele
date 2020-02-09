//package com.hepengju.hekele.base.core.websocket;
//
//import com.alibaba.fastjson.support.spring.FastjsonSockJsMessageCodec;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
///**
// * websocket配置类
// *
// * @author he_pe
// */
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    /**
//     * JSR 356 (Java API for WebSocket), 仅仅SpringBoot内置容器才需要注入
//     */
//    //@Bean
//    //public ServerEndpointExporter serverEndpointExporter(){
//    //    return new ServerEndpointExporter();
//    //}
//
//    /**
//     * Spring的websocket
//     */
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        // html5 原生的webSocket
//        registry.addHandler(new WebSocketHandler(), "/sys/websocket")
//                .addInterceptors(new WebSocketHandshakeInterceptor())
//                .setAllowedOrigins("*");
//
//        // sockJs 也支持一下, 注意设置 SockJsMessageCodec, 否则报错: java.lang.IllegalStateException: A SockJsMessageCodec is required but not available: Add Jackson to the classpath, or configure a custom SockJsMessageCodec.
//        registry.addHandler(new WebSocketHandler(),"/sys/websocket/sockjs")
//                .addInterceptors(new WebSocketHandshakeInterceptor())
//                .setAllowedOrigins("*")
//                .withSockJS()
//                .setMessageCodec(new FastjsonSockJsMessageCodec())
//                ;
//
//    }
//
//
//}
