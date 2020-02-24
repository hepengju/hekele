package com.hepengju.hekele;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SpringMonitorApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringMonitorApp.class, args);
    }

}
