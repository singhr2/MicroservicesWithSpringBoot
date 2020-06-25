package com.github.singhr2.services.accountmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
   URL: (Please check for hostname and port# from Eureka)
   http://192.168.1.5:60653/api/account-management/health-check
 */
@RestController
@RequestMapping("/api/account-management")
public class AccountManagementRestController {
    private static final String INSTANCE_PORT_NUMBER = "local.server.port";
    @Autowired
    private Environment env;

    @GetMapping("/health-check")
    public String checkServiceHealth(){
        return "Service instance is up and running on port# " + env.getProperty(INSTANCE_PORT_NUMBER);
    }
}
