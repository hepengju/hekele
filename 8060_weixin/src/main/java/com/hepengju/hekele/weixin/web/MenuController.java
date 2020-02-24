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

        WxMenuButton btn10 = clickMenu("日期数字", "date_number");
        btn10.getSubButtons().add(clickMenu("日期","date"));
        btn10.getSubButtons().add(clickMenu("日期时间","dateTime"));
        btn10.getSubButtons().add(clickMenu("整数","integer"));
        btn10.getSubButtons().add(clickMenu("小数","double"));
        btn10.getSubButtons().add(clickMenu("自增","autoIncrement"));

        WxMenuButton btn20 = clickMenu("随机字符", "string");
        btn20.getSubButtons().add(clickMenu("字母","randomAlphabetic"));
        btn20.getSubButtons().add(clickMenu("数字","randomNumber"));
        btn20.getSubButtons().add(clickMenu("字母数字","randomAlphanumeric"));
        btn20.getSubButtons().add(clickMenu("汉字","randomChinese"));
        btn20.getSubButtons().add(clickMenu("枚举值","code"));

        WxMenuButton btn30 = clickMenu("定制化", "custom");
        btn30.getSubButtons().add(clickMenu("姓名","chineseName"));
        btn30.getSubButtons().add(clickMenu("手机号","mobile"));
        btn30.getSubButtons().add(clickMenu("身份证号","idCard"));
        btn30.getSubButtons().add(clickMenu("公司名称","companyName"));
        btn30.getSubButtons().add(clickMenu("地址","chinaAddress"));

        menu.getButtons().add(btn10);
        menu.getButtons().add(btn20);
        menu.getButtons().add(btn30);

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
