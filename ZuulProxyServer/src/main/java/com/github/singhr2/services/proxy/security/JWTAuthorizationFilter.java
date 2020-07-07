package com.github.singhr2.services.proxy.security;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.github.singhr2.services.proxy.security.SecurityConstants.PROPERTY_JWT_TOKEN_HEADER_NAME;
import static com.github.singhr2.services.proxy.security.SecurityConstants.PROPERTY_JWT_TOKEN_HEADER_VALUE_PREFIX;
import static com.github.singhr2.services.proxy.security.SecurityConstants.PROPERTY_JWT_TOKEN_SECRET;


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

        String jwtTokenHeaderName = env.getProperty(PROPERTY_JWT_TOKEN_HEADER_NAME);
        String jwtTokenHeaderValuePrefix = env.getProperty(PROPERTY_JWT_TOKEN_HEADER_VALUE_PREFIX);


        String jwtHeaderValue = request.getHeader( jwtTokenHeaderName );
        LOGGER.info("Jwt Token Header :" + jwtHeaderValue);

        //if null or don't start with 'Bearer ' then continue
        if (null == jwtHeaderValue || ! (jwtHeaderValue.startsWith( jwtTokenHeaderValuePrefix )) ) {
            LOGGER.info("Jwt Token Header is null OR not starting with \"" + jwtTokenHeaderValuePrefix + "\" ");
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

        String jwtTokenSecret = env.getProperty(PROPERTY_JWT_TOKEN_SECRET);
        String jwtTokenHeaderName = env.getProperty(PROPERTY_JWT_TOKEN_HEADER_NAME);
        String jwtTokenHeaderValuePrefix = env.getProperty(PROPERTY_JWT_TOKEN_HEADER_VALUE_PREFIX);
        String jwtToken = request.getHeader( jwtTokenHeaderName );

        LOGGER.info("---------------------------------------------");
        LOGGER.info("-=-> jwtTokenSecret :" + jwtTokenSecret);
        LOGGER.info("-=-> jwtTokenHeaderName :" + jwtTokenHeaderName);
        LOGGER.info("-=-> jwtTokenHeaderValuePrefix :" + jwtTokenHeaderValuePrefix);
        LOGGER.info("-=-> jwtToken :" + jwtToken);
        LOGGER.info("=============================================");


        if (null != jwtToken) {
            LOGGER.info("-=-> Validating JWT Token." );

            // parse the token.
            String userId = Jwts.parser()
                    // this should be same value as set in Users service (see UserAuthenticationFilter )
                    .setSigningKey( jwtTokenSecret )
                    .parseClaimsJws(jwtToken.replace(jwtTokenHeaderValuePrefix, ""))
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