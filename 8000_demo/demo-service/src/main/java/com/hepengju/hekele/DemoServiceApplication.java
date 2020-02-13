package com.hepengju.hekele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableZuulProxy
@EnableFeignClients
@SpringBootApplication
public class DemoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoServiceApplication.class, args);
    }

}
