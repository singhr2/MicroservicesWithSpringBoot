package com.github.singhr2.api.user.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.singhr2.api.user.dto.UserDTO;
import com.github.singhr2.api.user.model.LoginRequestModel;
import com.github.singhr2.api.user.service.UsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

/*
  This filter is configured in  UserWebSecurityConfigurer.configure()

 */
public class UserAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private UsersService usersService;
    private Environment environment;
    //private AuthenticationManager authenticationManager;

    //TODO Whay @Autowired is not required here ?
    public UserAuthenticationFilter(UsersService _usersService, Environment _environment, AuthenticationManager _authenticationManager) {
        this.usersService = _usersService;
        this.environment = _environment;

        //IMPORTANT :
        //this.authenticationManager = _authenticationManager;
        super.setAuthenticationManager( _authenticationManager );
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        ObjectMapper objectMapper = null;
        LoginRequestModel loginRqstModel = null;
        Authentication usrPwdAuthenticationToken = null;

        try {
            objectMapper = new ObjectMapper();

            loginRqstModel = objectMapper.readValue(
                    request.getInputStream(),
                    LoginRequestModel.class);

            usrPwdAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    loginRqstModel.getEmail(),  //principal
                    loginRqstModel.getPassword(), //credentials
                    new ArrayList<>()  //GrantedAuthorities
            );

            /*
            Note;
            We need to tell Spring Boot where to validate our credentials.
            In our case, it will be managed in Users service.
            Refer configure method in UserWebSecurityConfigurer
            */
            return getAuthenticationManager().authenticate(usrPwdAuthenticationToken);

        } catch (IOException e) {
            //e.printStackTrace();
            LOGGER.error("-=-> IOException occured in attemptAuthentication()", e);
            throw new RuntimeException(e);
        }
    }

    //SPring will call this method after successful authentication
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
    throws IOException, ServletException {
        // Note: We use email-id as username while login
        String emailIdAsUsername = ((User) authResult.getPrincipal()).getUsername();
        LOGGER.info("-=-> Successfully authenticated : " + emailIdAsUsername);

        //call service to get the user details
        UserDTO userDTO = usersService.getUserDetailsByEmailId(emailIdAsUsername);
        LOGGER.info("-=-> userDTO loaded :" + userDTO);

        //String secretKeyToGenerateJwtToken = SecurityConstants.SECRET_KEY_TO_GENERATE_JWT;

        Long jwt_token_validity_duration = SecurityConstants.JWT_TOKEN_VALIDE_FOR;
        LOGGER.info("-=-> JWT token_expiration_time : " + jwt_token_validity_duration);

        LOGGER.info("-=-> Generating JWT Token ...");
        String jwsAccessToken = Jwts.builder()
                //.setIssuer("Ranbir Singh")
                .setSubject(userDTO.getUserId())
                //.claim("name", "Ranbir Singh")
                //.claim("scope", "admin")
                // *** For epoch to human readable format : www.epochconverter.com
                //.setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                //.setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                //.setExpiration(Date.from(Instant.ofEpochSecond(4622470422L)))
                //.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(token_expiration_time).toInstant()))
                //.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(token_expiration_time)))
                .setExpiration(Date.from(ZonedDateTime.now().plus(jwt_token_validity_duration, ChronoUnit.DAYS).toInstant()))
                .signWith(
                        SignatureAlgorithm.HS512,
                        //TextCodec.BASE64.decode(environment.getProperty(JWT_TOKEN_SECRET))
                        // This value is also referred in JWTAuthorizationFilter (zuul proxy)
                        SecurityConstants.SECRET_KEY_TO_GENERATE_JWT
                )
                .compact();

        LOGGER.info("-=-> JWT Token generated :" + jwsAccessToken);

        //<IMPORTANT>
        // Note that when adding the header value in Postman/Boomrang
        // We don't need to add the prefix 'Bearer ' as it is added here.
        response.addHeader(
                SecurityConstants.HEADER_NAME_FOR_JWT_TOKEN,
                SecurityConstants.JWT_TOKEN_PREFIX + jwsAccessToken);

        LOGGER.info("JwtToken in Header :" + response.getHeader(SecurityConstants.HEADER_NAME_FOR_JWT_TOKEN));

        //commented as don't see being referred anywhere
        //response.setHeader(SecurityConstants.HEADER_NAME_FOR_USERNAME, userDTO.getUserId());
    }

    /*
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
    throws IOException, ServletException {
        if (failed instanceof AccessTokenRequiredException) {
            // Need to force a redirect via the OAuth client filter, so rethrow here
            throw failed;
        }
        else {
            // If the exception is not a Spring Security exception this will result in a default error page
            super.unsuccessfulAuthentication(request, response, failed);
        }
    }
     */
}