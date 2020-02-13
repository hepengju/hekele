package com.hepengju.hekele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DataServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(DataServiceApp.class, args);
    }

}
