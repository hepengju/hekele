package com.hepengju.hekele.admin.web;


import com.hepengju.hekele.base.util.ExecUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 管理员控制
 *
 * @author he_pe 2019-12-25
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Value("${hekele.restart.shell}")
    private String restartShell;
    /**
     * Git拉取最新版本, 然后重启
     */
    @GetMapping("/gitPullThenRestart")
    public String gitPullThenRestart() {
        ExecUtil.execCommand(restartShell);
        return "index";
    }

}
