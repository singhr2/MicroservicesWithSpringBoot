package com.github.singhr2.api.user.security;

//---------------------------------------------
// These values are referred in
//      JWTAuthorizationFilter (Zuul Proxy Module)
// as well as in
//      UserAuthenticationFilter (Users Service)

// IF CHANGING HERE, DO CHANGE IN OTHER MODULE AS WELL
//---------------------------------------------

public class SecurityConstants {
    public static final String PROPERTY_ZUUL_GATEWAY_IP = "zuul.gateway.ip";

     //Secret Key to generate JWTs
    public static final String PROPERTY_JWT_TOKEN_SECRET = "jwt.token.secret";

    // currently using unit in Minutes (see UserAuthenticationFilter.successfulAuthentication()
    public static final String PROPERTY_JWT_TOKEN_VALIDE_FOR = "jwt.token.valid.duration";

    public static final String PROPERTY_JWT_TOKEN_HEADER_VALUE_PREFIX = "jwt.token.header.value.prefix";

    public static final String PROPERTY_JWT_TOKEN_HEADER_NAME = "jwt.token.header.name";

    //Added to test enc/decryption
    public static final String PROPERTY_ENCRYPTED_VALUE = "custom.encrypted.value";

    // URLs referenced in code
    // Note: This is defined in 'UsersController' (path = "/sign-up")
    //public static final String PROPERTY_URL_USERS_SIGN_UP = "url.users.signup";

    //Note: These can be changed in application.properties
    //public static final String PROPERTY_URL_H2_CONSOLE ="url.h2.console";

    //service called from users service
    //public static final String PROPERTY_URL_FOR_EXTERNAL_SERVICE = "url.external.service";
}