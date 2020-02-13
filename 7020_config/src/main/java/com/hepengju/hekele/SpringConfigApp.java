package com.hepengju.hekele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class SpringConfigApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfigApp.class, args);
    }

}