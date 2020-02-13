package com.hepengju.hekele;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SpringMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMonitorApplication.class, args);
    }

}
