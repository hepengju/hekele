package com.hepengju.hekele.weixin.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "Hello, Winxin Service";
    }
}
