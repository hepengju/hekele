//package com.hepengju.hekele.base.core.websocket;
//
//import com.hepengju.hekele.base.core.Now;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
//
//import java.util.Map;
//
///**
// * 握手拦截器, 记入当前用户是谁
// *
// * @author he_pe
// */
//public class WebSocketHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
//        super.beforeHandshake(request, response, wsHandler, attributes);
//       // String account = ShiroUtil.getActiveAccount();
//        //ActiveUser activeUser = ShiroUtil.getActiveUser();
//
//        attributes.put("account", Now.userCode());
//        attributes.put("activeUser", Now.user());
//        return true;
//    }
//}
