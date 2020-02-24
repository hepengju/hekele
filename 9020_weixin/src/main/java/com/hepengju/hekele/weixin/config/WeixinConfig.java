package com.hepengju.hekele.weixin.config;

import com.hepengju.hekele.weixin.handler.LogHandler;
import com.hepengju.hekele.weixin.handler.MenuHandler;
import com.hepengju.hekele.weixin.handler.MsgHandler;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType.CLICK;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WeixinProperties.class)
public class WeixinConfig {

    private final LogHandler logHandler;
    private final MenuHandler menuHandler;
    private final MsgHandler msgHandler;

    private final WeixinProperties properties;

    @Bean
    public WxMpService wxMpService() {
        // 代码里 getConfigs()处报错的同学，请注意仔细阅读项目说明，你的IDE需要引入lombok插件！！！！
        final List<WeixinProperties.MpConfig> configs = this.properties.getConfigs();

        WxMpService service = new WxMpServiceImpl();
        service.setMultiConfigStorages(configs
                .stream().map(a -> {
                    WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
                    configStorage.setAppId(a.getAppId());
                    configStorage.setSecret(a.getSecret());
                    configStorage.setToken(a.getToken());
                    configStorage.setAesKey(a.getAesKey());
                    return configStorage;
                }).collect(Collectors.toMap(WxMpDefaultConfigImpl::getAppId, a -> a, (o, n) -> o)));
        return service;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 记录所有事件的日志 （异步执行）
        newRouter.rule().handler(this.logHandler).next();

        // 接收客服会话管理事件
        //newRouter.rule().async(false).msgType(EVENT).event(KF_CREATE_SESSION)
        //        .handler(this.kfSessionHandler).end();
        //newRouter.rule().async(false).msgType(EVENT).event(KF_CLOSE_SESSION)
        //        .handler(this.kfSessionHandler).end();
        //newRouter.rule().async(false).msgType(EVENT).event(KF_SWITCH_SESSION)
        //        .handler(this.kfSessionHandler).end();

        // 门店审核事件
        //newRouter.rule().async(false).msgType(EVENT).event(POI_CHECK_NOTIFY).handler(this.storeCheckNotifyHandler).end();

        // 自定义菜单事件
        newRouter.rule().async(false).msgType(EVENT).event(CLICK).handler(this.menuHandler).end();

        // 点击菜单连接事件
        //newRouter.rule().async(false).msgType(EVENT).event(VIEW).handler(this.nullHandler).end();

        // 关注事件
        //newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();

        // 取消关注事件
        //newRouter.rule().async(false).msgType(EVENT).event(UNSUBSCRIBE).handler(this.unsubscribeHandler).end();

        // 上报地理位置事件
        //newRouter.rule().async(false).msgType(EVENT).event(EventType.LOCATION).handler(this.locationHandler).end();

        // 接收地理位置消息
        //newRouter.rule().async(false).msgType(XmlMsgType.LOCATION).handler(this.locationHandler).end();

        // 扫码事件
        //newRouter.rule().async(false).msgType(EVENT).event(EventType.SCAN).handler(this.scanHandler).end();

        // 默认
        newRouter.rule().async(false).handler(this.msgHandler).end();

        return newRouter;
    }
}
