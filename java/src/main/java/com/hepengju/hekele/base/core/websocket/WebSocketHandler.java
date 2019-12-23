//package com.hepengju.hekele.base.core.websocket;
//
//import com.topcheer.oss.base.util.StackUtil;
//import com.topcheer.oss.base.util.WebSocketUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * WebSocket处理器
// *
// * <pre>
// *  需求简述:
// *      1. 除了支持标准的websocket, 也支持sockJS, 以便处理兼容问题
// *      2. 需要实现向所有人通知和指定人通知的功能
// *
// *  其他说明:
// *     1. 实测是多例的(即使没有配置@Scope)
// *     2. 访问路径配置为需要认证, 即登录即可访问(shiro中配置)
// *
// * 修正: 由于系统需要多个WebSocket连接, 而操作几乎是一样的, 因此将其抽取到WebSocketUtil中
// * </pre>
// *
// * @author he_pe
// */
//public class WebSocketHandler extends TextWebSocketHandler {
//
//    private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);
//    /**
//     * 记录用户会话的Map
//     *  key: 当前用户的account, 对应ShiroUtil#getActiveAccount
//     *  value: 这个用户建立的webSocket连接集合
//     */
//    private static ConcurrentHashMap<String, List<WebSocketSession>> accountSessionMap = new ConcurrentHashMap<>();
//    public static ConcurrentHashMap<String, List<WebSocketSession>> getAccountSessionMap() {
//        return accountSessionMap;
//    }
//
//    /**
//     * 收到信息, 原封不动的返回
//     */
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        session.sendMessage(message);
//    }
//
//    /**
//     * 建立连接成功, 则放到Map中
//     */
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//       WebSocketUtil.afterConnectionEstablished(accountSessionMap, session);
//    }
//
//    /**
//     * 连接关闭后, 移出Map
//     */
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        WebSocketUtil.afterConnectionClosed(accountSessionMap, session);
//    }
//
//
//    @Override
//    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//        logger.error(StackUtil.getStackTrace(exception));
//    }
//}
//
//// 单独的版本
////@Component
////public class WebSocketHandler extends TextWebSocketHandler {
////
////    /**
////     * 记录用户会话的Map
////     *  key: 当前用户的account, 对应ShiroUtil#getActiveAccount
////     *  value: 这个用户建立的webSocket连接id集合
////     */
////    private static ConcurrentHashMap<String, List<WebSocketSession>> accountSessionMap = new ConcurrentHashMap<>();
////
////    /**
////     * 给所有人发送消息
////     */
////    public static void sendMessage(Object message) {
////        sendMessage(message, null);
////    }
////
////    /**
////     * 给指定用户发送消息
////     */
////    public static void sendMessage(Object message, String account) {
////        TextMessage textMessage = new TextMessage(JSON.toJSONString(message));
////        if(StringUtils.isBlank(account)){
////            accountSessionMap.forEach((a, sessionList) -> {
////                sendTextMessageToSessionList(textMessage, sessionList);
////            });
////        }else{
////            List<WebSocketSession> sessionList = accountSessionMap.get(account);
////            if (sessionList != null) {
////                sendTextMessageToSessionList(textMessage, sessionList);
////            }
////        }
////    }
////
////    private static void sendTextMessageToSessionList(TextMessage textMessage, List<WebSocketSession> sessionList) {
////        sessionList.forEach(session -> {
////            try {
////                session.sendMessage(textMessage);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        });
////    }
////
////    /**
////     * 收到信息, 原封不动的返回
////     */
////    @Override
////    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
////        session.sendMessage(message);
////    }
////
////    /**
////     * 建立连接成功, 则放到Map中
////     */
////    @Override
////    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
////        //String account = ShiroUtil.getActiveAccount();
////        String account = (String)session.getAttributes().get("account");
////
////        if(accountSessionMap.containsKey(account)){
////            accountSessionMap.get(account).add(session);
////        }else{
////            List<WebSocketSession> sessionList = new ArrayList<>(1);
////            sessionList.add(session);
////            accountSessionMap.put(account, sessionList);
////        }
////    }
////
////    /**
////     * 连接关闭后, 移出Map
////     */
////    @Override
////    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
////        //String account = ShiroUtil.getActiveAccount();
////        String account = (String)session.getAttributes().get("account");
////
////        List<WebSocketSession> sessionList = accountSessionMap.get(account);
////        if(sessionList != null){
////            sessionList.remove(session);
////            if(sessionList.size() == 0){
////                accountSessionMap.remove(account);
////            }
////        }
////    }
////}
