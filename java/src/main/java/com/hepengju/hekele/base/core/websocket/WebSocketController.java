package com.hepengju.hekele.base.core.websocket;

import com.topcheer.oss.base.common.R;
import com.topcheer.oss.base.util.ShiroUtil;
import com.topcheer.oss.base.util.WebSocketUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端界面发送消息控制器
 */
@RestController
@RequestMapping("/sys/websocket")
public class WebSocketController {

    @PostMapping("/sendMessage")
    public R sendMessage(String message,@RequestParam(name = "account",required = false) String account){
        String name = ShiroUtil.getActiveUser().getUser().getName();
        WebSocketUtil.sendMessage(WebSocketHandler.getAccountSessionMap(), name + ":" + message, account);
        return R.ok();
    }

    @PostMapping("/sendMessageJSR356")
    public R sendMessageJSR356(String message,@RequestParam(name = "account",required = false) String account){
        String name = ShiroUtil.getActiveUser().getUser().getName();
        WebSocketUtil.sendAccountMessage(WebSocketHandlerJSR356.getAccountSessionMap(), name + ":" + message, account);
        return R.ok();
    }
}
