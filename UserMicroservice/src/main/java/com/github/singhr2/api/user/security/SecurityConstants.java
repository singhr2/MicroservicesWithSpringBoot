package com.github.singhr2.api.user.security;


public class SecurityConstants {
    //TODO Review and move to application.properties if required.

    //Secret Key to generate JWTs
    public static final String SECRET_KEY_TO_GENERATE_JWT = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";

    // 864_000_000L = no. of seconds in 10 days
    //public static final Long PROPERTY_JWT_TOKEN_VALIDE_FOR = 864_000_000L;
    public static final Long JWT_TOKEN_VALIDE_FOR = 1L;  // no. of day(s)

    public static final String JWT_TOKEN_PREFIX = "Bearer "; // DoNot delete SPACE at the end
    public static final String HEADER_NAME_FOR_JWT_TOKEN = "Authorization";

    public static final String HEADER_NAME_FOR_USERNAME = "LOGGED_IN_USER";

    // URLs referenced in code
    // This is defined in UsersController and used in UserWebSecurityConfigurer
    public static final String USERS_SIGN_UP_URL = "/api/users/sign-up";
    //Note: These can be changed in application.properties
    public static final String H2_CONSOLE_URL = "/h2-console/**";
    public static final String ZUUL_GATEWAY_IP = "zuul.gateway.ip";


}
