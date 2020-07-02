package com.github.singhr2.api.user.security;

import com.github.singhr2.api.user.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

import static com.github.singhr2.api.user.security.SecurityConstants.USERS_SIGN_UP_URL;
import static com.github.singhr2.api.user.security.SecurityConstants.H2_CONSOLE_URL;
import static com.github.singhr2.api.user.security.SecurityConstants.ZUUL_GATEWAY_IP;

/*
 Note: This class will be scanned at start-up time.

 @Configuration :
  Tags the class as a source of bean definitions for the application context.

 @EnableWebSecurity :
  enable Spring Security’s web security support and provide the Spring MVC integration.

*/

// When spring security dependency is added in pom.xml,
//and we try to send any request to create user, it will return unauthorized
//We are adding login to handle this scenario.

@Configuration
@EnableWebSecurity
public class UserWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Environment environment;
    private UsersService usersService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserWebSecurityConfigurer(Environment _environment, UsersService _usersService, BCryptPasswordEncoder _bCryptPasswordEncoder) {
        this.environment = _environment;
        this.usersService = _usersService;
        this.bCryptPasswordEncoder = _bCryptPasswordEncoder;
    }


    // defines which URL paths should be secured and which should not.
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        String zuulProxyIpAddress = environment.getProperty(ZUUL_GATEWAY_IP);

        LOGGER.info("---> ---> ---> ---> ---> ---> ---> ---> ");
        LOGGER.info("---> Zuul Proxy Ip Address : " + zuulProxyIpAddress);
        LOGGER.info("<--- <--- <--- <--- <--- <--- <--- <--- >");

        //CSRF (Cross-Site Request Forgery) protection is enabled by default in the Java configuration.
        // The reason to disable CSRF is that the spring boot application is open to the public or it is cumbersome
        // when you are in under development or testing phase.

        //httpSecurity.csrf().disable();
        httpSecurity.cors()
        .and()
        .csrf().disable();

        // By default, Spring Security will block h2_console path of H2 database when you enable Spring Security
        //H2 database console runs inside a frame, So we need to disable X-Frame-Options in Spring Security.
        // Alternate option in 'application.properties' is to add ->  security.headers.frame=false
        // OPTIONS -> disable() / sameOrigin()
        httpSecurity.headers().frameOptions().disable();

        //{option-1} Allow all IP address -- TRY THIS AS FALLBACK OPTION IS SERVICE IS NOT WORKING
        /*
        httpSecurity
        .authorizeRequests().antMatchers(USER_SERVICE_URI).permitAll()
        // .and()
        // .authorizeRequests().antMatchers(H2_CONSOLE_URL).permitAll()
        ;
        */

        httpSecurity
        // Allow requests only from Zuul Proxy (API Gateway)
        .authorizeRequests().antMatchers("/**")
        .hasIpAddress(zuulProxyIpAddress)
        // add custom filter for User authentication
        .and()
        .addFilter( getUserAuthenticationFilter() );
    }


    private Filter getUserAuthenticationFilter() throws Exception {
        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter(usersService, environment, authenticationManager() );

        //-------------------------------------------------
        //{ OPTIONAL } CUSTOMIZING LOGIN URL
        //-------------------------------------------------
        // By default, spring framework provides login url as '/login'
        //TO customize it, we can execute below code
        //
        //Keep the below code Commented if you still want to continue using /login as url.

        // <WARNING>
        // If the property in application.properties is not set/commented,
        //then below line will throw an error at start time.

        //customAuthnFilter.setFilterProcessesUrl(environment.getProperty(PROPERTY_CUSTOM_LOGIN_URL));

        return userAuthenticationFilter;
    }


    /*
    Here, we are specifying the source for validating the login informaiton.
    Also, what encryption is used.

    The creds (email-id, password) are present in UserAuthenticationFilter.attemptAuthentication()
    */

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //sets up a basic in-memory authentication config. {unconfirmed}
        //auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");

        auth
        .userDetailsService( usersService )
        .passwordEncoder( bCryptPasswordEncoder);

        LOGGER.info("-=-> configure() invoked to set Userverification service and Encoder");
    }
}