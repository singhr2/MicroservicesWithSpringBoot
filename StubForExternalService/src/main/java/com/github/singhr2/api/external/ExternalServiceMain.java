package com.github.singhr2.api.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableEurekaClient
//@RefreshScope
public class ExternalServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(ExternalServiceMain.class, args);
    }
}