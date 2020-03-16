package com.hepengju.hekele.weixin.handler;

import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.data.client.DataFeignClient;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType.VIEW;

@Component @AllArgsConstructor
public class MenuHandler implements WxMpMessageHandler {

    private DataFeignClient dataFeignClient;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        if (VIEW.equals(wxMessage.getEvent())) {
            return null;
        }

        JsonR<List<String>> jsonR = dataFeignClient.getData(wxMessage.getEventKey(), 10);
        String result = jsonR.getData().stream().collect(Collectors.joining("\n"));

        return WxMpXmlOutMessage.TEXT().content(result)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
    }
}
