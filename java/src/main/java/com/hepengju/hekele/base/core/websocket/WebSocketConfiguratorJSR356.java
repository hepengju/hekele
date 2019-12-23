//package com.hepengju.hekele.base.core.websocket;
//
//import com.topcheer.oss.base.util.ShiroUtil;
//
//import javax.websocket.HandshakeResponse;
//import javax.websocket.server.HandshakeRequest;
//import javax.websocket.server.ServerEndpointConfig;
//import java.util.Map;
//
///**
// * JSR356, 记入当前用户是谁
// *
// * @author he_pe
// *
// */
//public class WebSocketConfiguratorJSR356 extends ServerEndpointConfig.Configurator{
//    @Override
//    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//        super.modifyHandshake(sec, request, response);
//        Map<String, Object> userProperties = sec.getUserProperties();
//        userProperties.put("userId", ShiroUtil.getActiveUser().getUser().getId());
//        userProperties.put("account", ShiroUtil.getActiveAccount());
//        userProperties.put("activeUser", ShiroUtil.getActiveUser());
//    }
//}
