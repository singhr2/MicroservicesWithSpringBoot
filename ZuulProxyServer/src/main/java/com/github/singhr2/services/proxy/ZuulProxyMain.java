package com.github.singhr2.services.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 *
 * ZUUL API Gateway using Netflix Zuul edge service library
 *  build a reverse proxy application that uses Netflix Zuul to forward requests
 *  to the service application.
 *  Use Zuul to filter requests that are made through the proxy service.
 *
 *  Create an Edge Service
 *  Spring Cloud Netflix includes an embedded Zuul proxy,
 *  which you can enable with the @EnableZuulProxy annotation.
 *  This will turn the Gateway application into a reverse proxy
 *  that forwards relevant calls to other services — such as our User, AccountManagement application.
 *
 *  Maintaining an API-Gateway service brings you more benefits
 *  other than load balancing like below:
 *    Authentication and Security
 *    Monitoring
 *    Dynamic Routing
 *    Static Response handling
 *    Rate limiting
 *
 *  Note: Zuul api gateway is also a Eureka client
 *        Zuul is also known as Edge Server
 */

/**
 * URLS for the services (e.g., Users, Account Management) will be :
 * http://<ZUUL-IP_AND_PORT_FROM_EUREKA>/<Service_Application-ID_from_EUREKA_(lowercase)>/resourceURL-from_controller
 * e.g., http://192.168.1.5:8011/acctmgmt-service/api/account-management/health-check
 *  Where, http://192.168.1.5:8011 : Zuul URL (in Eureka)
 *         /acctmgmt-service : Application id (in eureka)
 *         /api/account-management/health-check (Resource URL in controller)
 */

/*
   [Testing ribbon with zuul]
   -> In application.properties of target microservice, set the value of 'eureka.instance.instance-id' to uniquely identify each instance.
   -> Run multiple instances of the microservice and confirm the instance-ids in Eureka console
   -> Hit the URL of any method of the service (prefer something like showing the port# of instance)
   -> On refreshing, the port# should change indicating being returned from different service instance.
*/

@SpringBootApplication
//@EnableDiscoveryClient
@EnableEurekaClient
@EnableZuulProxy
public class ZuulProxyMain {
    public static void main(String[] args) {
        SpringApplication.run(ZuulProxyMain.class, args);
    }
}
