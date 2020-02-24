package com.hepengju.hekele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@EnableHystrixDashboard
@SpringBootApplication
public class SpringHystrixDashboardApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringHystrixDashboardApp.class, args);
    }

}
