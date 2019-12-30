package com.hepengju.hekele.admin.web;


import com.hepengju.hekele.base.core.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 管理员控制
 *
 * @author he_pe 2019-12-25
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Value("${hekele.restart.shell}")
    private String restartShell;
    /**
     * Git拉取最新版本, 然后重启
     */
    @GetMapping("/gitPullThenRestart")
    public R gitPullThenRestart() throws IOException {
        Runtime.getRuntime().exec(restartShell);
        return R.ok().setErrmsg("正在重新启动, 请稍等");
    }

}
