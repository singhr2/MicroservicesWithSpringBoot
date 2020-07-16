package com.github.singhr2.services.proxy.security;

//---------------------------------------------
// Some of these properties  are referred in
//      JWTAuthorizationFilter (Zuul Proxy Module)
// as well as in
//      UserAuthenticationFilter (Users Service)

// IF CHANGING HERE, DO CHANGE IN OTHER MODULE AS WELL
//---------------------------------------------

public class SecurityConstants {
    //Secret Key to generate JWTs
    public static final String PROPERTY_JWT_TOKEN_SECRET = "jwt.token.secret";

    public static final String PROPERTY_JWT_TOKEN_HEADER_VALUE_PREFIX = "jwt.token.header.value.prefix";

    public static final String PROPERTY_JWT_TOKEN_HEADER_NAME = "jwt.token.header.name";

    //                  *** URLs referenced in code ***
    //Note: These can be changed in application.properties

    public static final String PROPERTY_URL_H2_CONSOLE ="url.h2.console";

    public static final String PROPERTY_URL_USERS_SRVC_ACTUATOR = "url.users.actuator";

    public static final String PROPERTY_URL_ZUUL_PROXY_ACTUATOR = "url.zuul.actuator";

    // Note: This is defined in 'UsersController' (path = "/sign-up")
    // and used in UserWebSecurityConfigurer
    public static final String PROPERTY_URL_USERS_SIGN_IN = "url.users.signin"; // Login

    // Note: the url might change if have set custom login url in
    // UserWebSecurityConfigurer.getUserAuthenticationFilter()
    // By default, spring provides '/login' as endpoint url
    public static final String PROPERTY_URL_NEW_USER_REGISTRATION = "url.users.signup"; // New User Regd.

    //service called from users service
    public static final String PROPERTY_URL_FOR_EXTERNAL_SERVICE = "url.external.service";
}