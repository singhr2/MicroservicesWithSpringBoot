package com.github.singhr2.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class MyConfigServerMain {
    public static void main(String[] args) {
        SpringApplication.run(MyConfigServerMain.class, args);
    }
}
