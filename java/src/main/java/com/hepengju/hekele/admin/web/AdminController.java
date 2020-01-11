package com.hepengju.hekele.admin.web;


import com.hepengju.hekele.base.core.Now;
import com.hepengju.hekele.base.core.R;
import com.hepengju.hekele.base.util.ExecUtil;
import com.hepengju.hekele.base.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员控制
 *
 * @author he_pe 2019-12-25
 */
@Api(tags = "超级管理员操作")
@Slf4j
@RestController
@RequestMapping("/admin/super")
public class AdminController {

    private ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String hekeleXlsmFile = "classpath:public/db/hekele.xlsm";

    @Value("${hekele.restart.shell}")
    private String restartShell;

    @ApiOperation("拉取最新代码然后重启应用")
    @GetMapping("gitPullThenRestart")
    public R gitPullThenRestart() {
        // 必须开启一个新线程, 从而返回给浏览器, 否则浏览器得不到响应, 还会再次发送
        new Thread(() -> ExecUtil.execCommand(restartShell)).start();
        return R.ok().setErrmsg("正在重新启动, 请稍等");
    }

    @ApiOperation("下载hekele最新数据库设计")
    @GetMapping("downloadHekeleXlsm")
    public void downloadHekeleXlsm(HttpServletResponse res) throws IOException {
        WebUtil.handleFileDownload("hekele-" + Now.yyyyMMddHHmmss() + ".xlsm",
                resourceLoader.getResource(hekeleXlsmFile).getInputStream());
    }

}
