package com.hepengju.hekele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableHystrix
@EnableZuulProxy
@EnableFeignClients
@SpringBootApplication
public class DemoServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(DemoServiceApp.class, args);
    }

}
