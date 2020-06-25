package com.github.singhr2.services.accountmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AccountManagementMain {
    public static void main(String[] args) {
        SpringApplication.run(AccountManagementMain.class, args);
    }
}
