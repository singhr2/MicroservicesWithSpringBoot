package com.github.singhr2.api.user.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
 Note: This class will be scanned at start-up time.

 @Configuration :
  Tags the class as a source of bean definitions for the application context.

 @EnableWebSecurity :
  enable Spring Security’s web security support and provide the Spring MVC integration.

*/

@Configuration
@EnableWebSecurity
public class UserWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserWebSecurityConfigurer.class);

    //TODO This should match with the RequestMapping in User service controller
    private static final String USER_SERVICE_URI = "/api/users/**";

    //Note: This can be changed in application.properties
    private static final String H2_CONSOLE_URL = "/h2-console/**";


    /*
    TO fetch the value from application.properties, use the @Value annotation
    OR
    Use org.springframework.core.env.Environment;
    sample code :
        private Environment env;
        public UserWebSecurityConfigurer(Environment pEnv) {
            env= pEnv;
        }
        .
        env.getProperty("zuul.proxy.ip.address")
     */

    //@Value("${zuul.proxy.ip.address}")
    @Value("${zuul_ip}")
    private String zuulProxyIpAddress;

    /*
        defines which URL paths should be secured and which should not.
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        //TODO Change from info to debug.

        LOGGER.info("---> ---> ---> ---> ---> ---> ---> ---> ");
        LOGGER.info("---> Zuul Proxy Ip Address : " + zuulProxyIpAddress);
        LOGGER.info("<--- <--- <--- <--- <--- <--- <--- <--- >");

        //super.configure(http);

        //{option-1} Allow all IP address
        /*
        httpSecurity
        .authorizeRequests().antMatchers(USER_SERVICE_URI).permitAll()
        // .and()
        // .authorizeRequests().antMatchers(H2_CONSOLE_URL).permitAll()
        ;
        */

        //{option-2} Allow IP address from Zuul Proxy /API Gateway
        httpSecurity
        .authorizeRequests().antMatchers("/**")
        .hasIpAddress(zuulProxyIpAddress);


        // { WARNING }
        //------------------------------------------------------------
        //Don't use this configuration in a production environment.
        //------------------------------------------------------------

        //CSRF (Cross-Site Request Forgery) protection is enabled by default
        // in the Java configuration. The reason to disable CSRF is that the spring boot
        // application is open to the public or it is cumbersome
        // when you are in under development or testing phase.
        httpSecurity.csrf().disable();


        // By default, Spring Security will block /h2_console path of H2 database
        // when you enable Spring Security
        //H2 database console runs inside a frame,
        // So we need to disable X-Frame-Options in Spring Security.
        // Alternate option: In 'application.properties'
        // security.headers.frame=false
        httpSecurity.headers().frameOptions().disable();


        // ??
        //httpSecurity.headers().frameOptions().sameOrigin();
    }
}