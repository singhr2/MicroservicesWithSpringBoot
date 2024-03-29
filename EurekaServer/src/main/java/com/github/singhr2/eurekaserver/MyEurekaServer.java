package com.github.singhr2.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MyEurekaServer {
    public static void main(String[] args) {
        SpringApplication.run(MyEurekaServer.class, args);
    }
}
