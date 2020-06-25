package com.github.singhr2.api.user.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

/*
    Reference : https://reflectoring.io/spring-security-password-handling/
*/
@Configuration
public class PasswordEncodingUtil {

    @Bean
    //@Bean(name= {"MyEncoderBean1","MyBean2"}, initMethod="init", destroyMethod="destroy")
    public PasswordEncoder getPasswordEncoder(){
        //BCryptPasswordEncoder has the parameter strength.
        // The default value in Spring Security is 10.
        // It’s recommended to use a SecureRandom as salt generator,
        // because it provides a cryptographically strong random number.

        int strength = 10; // work factor of bcrypt

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(strength, new SecureRandom());

        return bCryptPasswordEncoder;
    }
}
