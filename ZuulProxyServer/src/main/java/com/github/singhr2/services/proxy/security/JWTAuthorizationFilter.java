package com.github.singhr2.services.proxy.security;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.github.singhr2.services.proxy.security.SecurityConstants.HEADER_NAME_FOR_JWT_TOKEN;
import static com.github.singhr2.services.proxy.security.SecurityConstants.JWT_TOKEN_PREFIX;
import static com.github.singhr2.services.proxy.security.SecurityConstants.SECRET_KEY_TO_GENERATE_JWT;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * As we have implemented the filter (UserAuthenticationFilter) responsible for authenticating users,
 * we now need to implement the filter responsible for user authorization.
 * We create this filter here.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private Environment env;
    public JWTAuthorizationFilter(AuthenticationManager authManager, Environment _env) {
        super(authManager);
        this.env = _env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        LOGGER.info("---------------------------------------------");
        LOGGER.info("entered doFilterInternal() method of JWTAuthorizationFilter ...");
        LOGGER.info("=============================================");

        String jwtHeaderValue = request.getHeader(HEADER_NAME_FOR_JWT_TOKEN);
        LOGGER.info("Jwt Token Header :" + jwtHeaderValue);

        //if null or don't start with 'Bearer ' then continue
        if (null == jwtHeaderValue || ! (jwtHeaderValue.startsWith(JWT_TOKEN_PREFIX)) ) {
            LOGGER.info("Jwt Token Header is null OR not starting with \"" + JWT_TOKEN_PREFIX + "\" ");
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        LOGGER.info("---------------------------------------------");
        LOGGER.info("entered getAuthentication() method of JWTAuthorizationFilter ...");
        LOGGER.info("=============================================");

        String jwtToken = request.getHeader(HEADER_NAME_FOR_JWT_TOKEN);
        LOGGER.info("-=-> jwtToken :" + jwtToken);


        if (null != jwtToken) {
            // parse the token.
            String userId = Jwts.parser()
                    // this should be same value as set in Users service (see UserAuthenticationFilter )
                    .setSigningKey(SECRET_KEY_TO_GENERATE_JWT)  //environment.getProperty(SECRET_KEY_TO_GENERATE_JWT)
                    .parseClaimsJws(jwtToken.replace(JWT_TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (null != userId) {
                // principal, credentials, grantedAuthorities
                LOGGER.info("-=-> Returning instance of UsernamePasswordAuthenticationToken");
                return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
            }

            LOGGER.info("-=-> returning null : checkpoint-1");
            return null;
        }

        LOGGER.info("-=-> returning null : checkpoint-2");
        return null;
    }
}