package com.hepengju.hekele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

//@EnableZuulProxy --> 先不编写其他的，直接启用默认的Zuul服务
@EnableZuulServer
@SpringBootApplication
public class SpringZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringZuulApplication.class, args);
    }

}
