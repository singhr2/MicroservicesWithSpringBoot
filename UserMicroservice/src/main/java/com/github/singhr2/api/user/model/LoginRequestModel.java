package com.github.singhr2.api.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequestModel {
    private String email; // we are using email-id as username for login
    private String password;
}
