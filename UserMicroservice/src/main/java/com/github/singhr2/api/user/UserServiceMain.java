package com.github.singhr2.api.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/*
 * Note:
 * To allow running multiple instance,
 *     Run > Edit Configurations> Select the service and TICK 'Allow parallel run' checkbox
 *
 * To run from commmand-line,
 * step-1) Goto the directory containing pom.xml file
 *
 * step-2)
 *  2.1 : run the below command after changing spring.application.instance_id to unique value
 *   mvn spring-boot:run -Dspring-boot.run.arguments=--spring.application.instance_id=ranbir
 *
 *  2.2 : start the instance passing the port_no in parameter (PORT_NO is placeholder specified in application.properties)
 *   mvn spring-boot:run -Dspring-boot.run.arguments=--PORT_NO=9091
 *
 *  2.3 : start instance along with passing  PORT_NO and spring.application.instance_id both
 *   (Note-
 *      a. pass the values separated by SPACE and all values within 1 quotes)
 *      b. The port no. is visible when you click on the instance link in Eureka dashboard
 *
 *   mvn spring-boot:run -Dspring-boot.run.arguments="--PORT_NO=9091 --spring.application.instance_id=ranbir2"
 *
 */

/*
  [ CONNECTING TO H2 IM-MEMORY DATABASE ]
  ---------------------------------------
  -> Start Eureka and Zuul Proxy
  -> Start microservice (here User service)
  -> Form the URL e.g., http://192.168.1.5:8011/user-service/h2-console/
        where, http://192.168.1.5:8011  -> URL for Zuul Proxy (get by clicking on link in Eureka console)
        /user-service -> Application-Id (in lowercase) from Eureka console
        /h2-console/  -> URL of H2 console
 */

/*
   Verify db connection attributes in 'application.properties'
   i.e. "spring.datasource.*"

   -> Check the URL is H2-console and set the same in 'spring.datasource.url' property

   -> To create a NEW record, in REST Client e.g., Postman/ Boomerang:
       URL: http://192.168.1.5:8011/user-service/api/users   // Verify the IP-address and Port# for Zuul from Eureka Console
       Http Verb: POST
       Sample Request Msg (Request Body):
            {
                "firstName":"Ranbir4",
                "lastName":"Singh",
                "password":"12345678",
                "emailId":"demo2@test.com"
            }
    -> To view all records:
        URL: Same as above
        Http Verb: GET
*/

@SpringBootApplication
@EnableDiscoveryClient
//@EnableEurekaClient
public class UserServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceMain.class, args);
    }
}
