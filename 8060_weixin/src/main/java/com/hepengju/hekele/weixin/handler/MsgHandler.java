package com.hepengju.hekele.weixin.handler;

import com.hepengju.hekele.base.core.JsonR;
import com.hepengju.hekele.data.client.DataFeignClient;
import com.hepengju.hekele.weixin.builder.TextBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component @AllArgsConstructor @Slf4j
public class MsgHandler implements WxMpMessageHandler {

    private DataFeignClient dataFeignClient;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
                                    Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {

        String content = "暂无生成器: " + wxMessage.getContent();
        JsonR<List<String>> jsonR = dataFeignClient.getData(wxMessage.getContent(), 10);

        if(JsonR.isOk(jsonR)) {
            content = jsonR.getData().stream().collect(Collectors.joining("\n"));
        } else{
            log.error(content);
            try {
                String names = dataFeignClient.getGenNameList().getData().stream()
                        .sorted()
                        .collect(Collectors.joining(","));
                content += "\n可尝试以下关键字: \n" + names;
            } catch (Exception e) {
            }
        }
        return new TextBuilder().build(content, wxMessage, weixinService);

    }

}
