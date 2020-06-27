package com.hepengju.hekele.weixin.web;

import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static me.chanjar.weixin.common.api.WxConsts.MenuButtonType;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@AllArgsConstructor
@RestController
@RequestMapping("/wx/menu/{appid}")
public class MenuController {

    private final WxMpService wxService;

    @GetMapping("/create")
    public String menuCreateSample(@PathVariable String appid) throws WxErrorException {
        WxMenu menu = new WxMenu();

        WxMenuButton btn1 = clickMenu("基本", "gen01");
        btn1.getSubButtons().add(clickMenu("姓名","chineseName"));
        btn1.getSubButtons().add(clickMenu("手机号","mobile"));
        btn1.getSubButtons().add(clickMenu("身份证号","idCard"));
        btn1.getSubButtons().add(clickMenu("公司名称","companyName"));
        btn1.getSubButtons().add(clickMenu("地址","chinaAddress"));

        WxMenuButton btn2 = clickMenu("定制", "gen02");
        btn2.getSubButtons().add(clickMenu("UUID","uUID"));
        btn2.getSubButtons().add(clickMenu("车架号","carFrameNumber"));
        btn2.getSubButtons().add(clickMenu("电话号码","telephone"));
        btn2.getSubButtons().add(clickMenu("邮箱","email"));
        btn2.getSubButtons().add(clickMenu("密码","password"));

        WxMenuButton btn3 = clickMenu("其他", "gen03");
        btn3.getSubButtons().add(clickMenu("字母","randomAlphabetic"));
        btn3.getSubButtons().add(clickMenu("数字","randomNumber"));
        btn3.getSubButtons().add(clickMenu("字母数字","randomAlphanumeric"));
        btn3.getSubButtons().add(clickMenu("日期时间","dateTime"));
        btn3.getSubButtons().add(clickMenu("中国城市","chinaCity"));

        menu.getButtons().add(btn1);
        menu.getButtons().add(btn2);
        menu.getButtons().add(btn3);

        this.wxService.switchover(appid);
        return this.wxService.getMenuService().menuCreate(menu);
    }

    private WxMenuButton clickMenu(String name, String key) {
        WxMenuButton btn = new WxMenuButton();
        btn.setType(MenuButtonType.CLICK);
        btn.setName(name);
        btn.setKey(key);
        return btn;
    }
}
