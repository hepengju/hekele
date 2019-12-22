package com.hepengju.hekele.base.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket工具类
 *
 */
public class WebSocketUtil {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketUtil.class);

    private static final String USER_ID = "userId";
    private static final String ACCOUNT = "account";

    /* ************************************ JSR356的WebSocket (使用userId)************************************* */
    /**
     * 建立连接成功, 则放到Map中
     */
    public static synchronized void addUserIdSession(ConcurrentHashMap<Integer, List<Session>> accountSessionMap, Session session){
        Integer userId = (Integer)session.getUserProperties().get(USER_ID);
        if(accountSessionMap.containsKey(userId)){
            accountSessionMap.get(userId).add(session);
        }else{
            List<Session> sessionList = new ArrayList<>(1);
            sessionList.add(session);
            accountSessionMap.put(userId, sessionList);
        }
    }

    /**
     * 连接关闭后, 移出Map
     */
    public static synchronized void removeUserIdSession(ConcurrentHashMap<Integer, List<Session>> accountSessionMap, Session session){
        Integer userId = (Integer)session.getUserProperties().get(USER_ID);
        List<Session> sessionList = accountSessionMap.get(userId);
        if(sessionList != null){
            sessionList.remove(session);
            if(sessionList.size() == 0){
                accountSessionMap.remove(userId);
            }
        }
    }

    /**
     * 给所有人发送消息
     */
    public static synchronized void sendUserIdMessage(ConcurrentHashMap<Integer, List<Session>> userIdSessionMap, Object message) {
        sendUserIdMessage(userIdSessionMap, message, null);
    }

    /**
     * 给指定用户发送消息
     */
    public static synchronized void sendUserIdMessage(ConcurrentHashMap<Integer, List<Session>> userIdSessionMap, Object message, Integer userId) {
        String textMessage = JSON.toJSONString(message);
        if(userId == null){
            userIdSessionMap.forEach((a, sessionList) -> sendTextMessageToSessionListJSR356(textMessage, sessionList));
        }else{
            List<Session> sessionList = userIdSessionMap.get(userId);
            if (sessionList != null) {
                sendTextMessageToSessionListJSR356(textMessage, sessionList);
            }
        }
    }

    /* ************************************ JSR356的WebSocket (使用account) ************************************* */
    // session.getOpenSessions()可以直接获得所有打开的session, 不必自己记录了, 直接写即可, 不用工具类
    /**
     * 建立连接成功, 则放到Map中
     */
    public static synchronized void addAccountSession(ConcurrentHashMap<String, List<Session>> accountSessionMap, Session session){
        String account = (String)session.getUserProperties().get(ACCOUNT);
        if(accountSessionMap.containsKey(account)){
            accountSessionMap.get(account).add(session);
        }else{
            List<Session> sessionList = new ArrayList<>(1);
            sessionList.add(session);
            accountSessionMap.put(account, sessionList);
        }
    }

    /**
     * 连接关闭后, 移出Map
     */
    public static synchronized void removeAccountSession(ConcurrentHashMap<String, List<Session>> accountSessionMap, Session session){
        String account = (String)session.getUserProperties().get(ACCOUNT);
        List<Session> sessionList = accountSessionMap.get(account);
        if(sessionList != null){
            sessionList.remove(session);
            if(sessionList.size() == 0){
                accountSessionMap.remove(account);
            }
        }
    }

    /**
     * 给所有人发送消息
     */
    public static synchronized void sendAccountMessage(ConcurrentHashMap<String, List<Session>> accountSessionMap, Object message) {
        sendAccountMessage(accountSessionMap, message, null);
    }

    /**
     * 给指定用户发送消息
     */
    public static synchronized void sendAccountMessage(ConcurrentHashMap<String, List<Session>> accountSessionMap, Object message, String account) {
        String textMessage = JSON.toJSONString(message);
        if(StringUtils.isBlank(account)){
            accountSessionMap.forEach((a, sessionList) -> sendTextMessageToSessionListJSR356(textMessage, sessionList));
        }else{
            List<Session> sessionList = accountSessionMap.get(account);
            if (sessionList != null) {
                sendTextMessageToSessionListJSR356(textMessage, sessionList);
            }
        }
    }

    private static synchronized void sendTextMessageToSessionListJSR356(String textMessage, List<Session> sessionList) {
        sessionList.forEach(session -> {
            try {
                session.getBasicRemote().sendText(textMessage);
            } catch (IOException e) {
                logger.error(StackUtil.getStackTrace(e));
            }
        });
    }

    /* ************************************ Spring的WebSocket ************************************* */
    /**
     * 建立连接成功, 则放到Map中
     */
    public static synchronized void afterConnectionEstablished(ConcurrentHashMap<String, List<WebSocketSession>> accountSessionMap, WebSocketSession session){
        String account = (String)session.getAttributes().get(ACCOUNT);
        if(accountSessionMap.containsKey(account)){
            accountSessionMap.get(account).add(session);
        }else{
            List<WebSocketSession> sessionList = new ArrayList<>(1);
            sessionList.add(session);
            accountSessionMap.put(account, sessionList);
        }
    }

    /**
     * 连接关闭后, 移出Map
     */
    public static synchronized void afterConnectionClosed(ConcurrentHashMap<String, List<WebSocketSession>> accountSessionMap, WebSocketSession session){
        String account = (String)session.getAttributes().get(ACCOUNT);
        List<WebSocketSession> sessionList = accountSessionMap.get(account);
        if(sessionList != null){
            sessionList.remove(session);
            if(sessionList.size() == 0){
                accountSessionMap.remove(account);
            }
        }
    }

    /**
     * 给所有人发送消息
     */
    public static synchronized void sendMessage(ConcurrentHashMap<String, List<WebSocketSession>> accountSessionMap, Object message) {
        sendMessage(accountSessionMap, message, null);
    }

    /**
     * 给指定用户发送消息
     */
    public static synchronized void sendMessage(ConcurrentHashMap<String, List<WebSocketSession>> accountSessionMap, Object message, String account) {
        TextMessage textMessage = new TextMessage(JSON.toJSONString(message));
        if(StringUtils.isBlank(account)){
            accountSessionMap.forEach((a, sessionList) -> sendTextMessageToSessionList(textMessage, sessionList));
        }else{
            List<WebSocketSession> sessionList = accountSessionMap.get(account);
            if (sessionList != null) {
                sendTextMessageToSessionList(textMessage, sessionList);
            }
        }
    }

    private static synchronized void sendTextMessageToSessionList(TextMessage textMessage, List<WebSocketSession> sessionList) {
        sessionList.forEach(session -> {
            try {
                session.sendMessage(textMessage);
            } catch (IOException e) {
                logger.error(StackUtil.getStackTrace(e));
            }
        });
    }

}
