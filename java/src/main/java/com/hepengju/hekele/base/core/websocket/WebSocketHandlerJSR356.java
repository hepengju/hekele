package com.hepengju.hekele.base.core.websocket;

import com.topcheer.oss.base.util.StackUtil;
import com.topcheer.oss.base.util.WebSocketUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSR 356 (Java API for WebSocket)
 *
 * <pre>
 *     1. 如果需要知道当前用户是谁, 需要配置configurator = WebSocketConfiguratorJSR356.class
 *     2. JSR356使用起来更方便(相比较与Spring的websocket), 但是不支持SockJS
 * </pre>
 *
 * <a href="https://www.oracle.com/technetwork/cn/articles/java/jsr356-1937161-zhs.html">JSR356官方链接</a>
 */
@ServerEndpoint(value = "/sys/websocket/jsr356", configurator = WebSocketConfiguratorJSR356.class)
@Component
public class WebSocketHandlerJSR356 {

    /**
     * 记录用户会话的Map
     *  key: 当前用户的account, 对应ShiroUtil#getActiveAccount
     *  value: 这个用户建立的webSocket连接集合
     */
    private static Logger logger = LoggerFactory.getLogger(WebSocketHandlerJSR356.class);
    private static ConcurrentHashMap<String, List<Session>> accountSessionMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, List<Session>> getAccountSessionMap() {
        return accountSessionMap;
    }

    @OnOpen
    public void onOpen(Session session) {
        WebSocketUtil.addAccountSession(accountSessionMap, session);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        WebSocketUtil.removeAccountSession(accountSessionMap, session);
    }

    /**
     * 收到消息, 原封不动的返回
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }

    @OnError
    public void onError(Session session, Throwable e) {
        logger.error(StackUtil.getStackTrace(e));
    }

}
