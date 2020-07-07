package com.github.singhr2.services.proxy.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static com.github.singhr2.services.proxy.security.SecurityConstants.PROPERTY_URL_H2_CONSOLE;
import static com.github.singhr2.services.proxy.security.SecurityConstants.PROPERTY_URL_NEW_USER_REGISTRATION;
import static com.github.singhr2.services.proxy.security.SecurityConstants.PROPERTY_URL_USERS_SIGN_IN;
import static com.github.singhr2.services.proxy.security.SecurityConstants.PROPERTY_URL_USERS_SRVC_ACTUATOR;
import static com.github.singhr2.services.proxy.security.SecurityConstants.PROPERTY_URL_ZUUL_PROXY_ACTUATOR;

@Configuration
@EnableWebSecurity
//@RefreshScope
public class ZuulGatewayWebSecurity extends WebSecurityConfigurerAdapter {
    private  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final Environment env;

    @Autowired
    public ZuulGatewayWebSecurity(Environment _env) {
        this.env = _env;
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String urlForH2Console = env.getProperty(PROPERTY_URL_H2_CONSOLE);
        String urlForUserSignUp = env.getProperty(PROPERTY_URL_NEW_USER_REGISTRATION);
        String urlForUserSignIn = env.getProperty(PROPERTY_URL_USERS_SIGN_IN);
        String urlForUserSrvcActuators = env.getProperty(PROPERTY_URL_USERS_SRVC_ACTUATOR);
        String urlForZuulActuators = env.getProperty(PROPERTY_URL_ZUUL_PROXY_ACTUATOR);

        LOGGER.info("---------------------------------------------");
        LOGGER.info("entered configure() method for Zuul proxy ...");
        LOGGER.info(">> api.h2console.url.path : " + urlForH2Console);
        LOGGER.info(">> api.registration.url.path {post} : " + urlForUserSignUp);
        LOGGER.info(">> api.login.url.path {post}: " + urlForUserSignIn);
        LOGGER.info(">> api.users.actuator.url.path : " + urlForUserSrvcActuators);
        LOGGER.info(">> api.zuul.actuator.url.path : " + urlForZuulActuators);
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
        .antMatchers( urlForUserSrvcActuators ).permitAll()
        .antMatchers( urlForZuulActuators ).permitAll()
        .antMatchers( urlForH2Console ).permitAll()
        .antMatchers(HttpMethod.POST, urlForUserSignUp ).permitAll()
        .antMatchers(HttpMethod.POST, urlForUserSignIn ).permitAll()
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