package com.hepengju.hekele;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@EnableTurbine
@SpringBootApplication
public class SpringTurbineApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringTurbineApp.class, args);
    }

}
