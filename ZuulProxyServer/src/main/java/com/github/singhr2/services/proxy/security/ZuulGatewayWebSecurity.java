package com.github.singhr2.services.proxy.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class ZuulGatewayWebSecurity extends WebSecurityConfigurerAdapter {
    private  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String PROPERTY_H2_CONSOLE_URL = "api.h2console.url.path";
    private static final String PROPERTY_NEW_USER_REGISTRATION_URL = "api.registration.url.path";
    private static final String PROPERTY_USERS_SRVC_ACTUATOR_URL = "api.users.actuator.url.path";
    private static final String PROPERTY_ZUUL_PROXY_ACTUATOR_URL = "api.zuul.actuator.url.path";

    // Note: the url might change if have set custom login url in
    // UserWebSecurityConfigurer.getUserAuthenticationFilter()
    // By default, spring provides /login
    private static final String PROPERTY_USER_LOGIN_URL ="api.login.url.path";


    private final Environment env;

    @Autowired
    public ZuulGatewayWebSecurity(Environment _env) {
        this.env = _env;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        LOGGER.info("---------------------------------------------");
        LOGGER.info("entered configure() method for Zuul proxy ...");
        LOGGER.info(">> api.h2console.url.path : " + env.getProperty(PROPERTY_H2_CONSOLE_URL));
        LOGGER.info(">> api.registration.url.path {post} : " + env.getProperty(PROPERTY_NEW_USER_REGISTRATION_URL));
        LOGGER.info(">> api.login.url.path {post}: " + env.getProperty(PROPERTY_USER_LOGIN_URL));
        LOGGER.info(">> api.users.actuator.url.path : " + env.getProperty(PROPERTY_USERS_SRVC_ACTUATOR_URL));
        LOGGER.info(">> api.zuul.actuator.url.path : " + env.getProperty(PROPERTY_ZUUL_PROXY_ACTUATOR_URL));
        LOGGER.info("=============================================");

        //refer UserWebSecurityConfigurer for documentation
        httpSecurity.cors()
        .and()
        .csrf().disable();

        //for H2-console which uses frames
        httpSecurity.headers().frameOptions().disable();

        httpSecurity.authorizeRequests()
        // H2 console, Login Page, New User Registration pages should be accessible to all
        //.antMatchers("/api/auth", "/api/users/me", "/api/greetings/public").permitAll()
        .antMatchers(env.getProperty(PROPERTY_USERS_SRVC_ACTUATOR_URL)).permitAll()
        .antMatchers(env.getProperty(PROPERTY_ZUUL_PROXY_ACTUATOR_URL)).permitAll()
        .antMatchers(env.getProperty(PROPERTY_H2_CONSOLE_URL)).permitAll()
        .antMatchers(HttpMethod.POST, env.getProperty(PROPERTY_NEW_USER_REGISTRATION_URL)).permitAll()
        .antMatchers(HttpMethod.POST, env.getProperty(PROPERTY_USER_LOGIN_URL)).permitAll()
        // any other request should be authenticated using the Filter
        .anyRequest().authenticated()
        .and()
        .addFilter(new JWTAuthorizationFilter (authenticationManager(), env));
        ;

        //HttpSession is used to uniquely identify client
        // Possible values : always / ifRequired / never / stateless(most strict)
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        LOGGER.info("***** httpSecurity :" + httpSecurity);
    }
}