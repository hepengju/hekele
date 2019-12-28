package com.hepengju.hekele.admin.web;

import com.hepengju.hekele.admin.service.CodeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "代码管理")
@RestController
@RequestMapping("/admin/code")
public class CodeController {

    @Autowired private CodeService codeService;


}
