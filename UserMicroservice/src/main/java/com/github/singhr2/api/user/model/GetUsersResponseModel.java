package com.github.singhr2.api.user.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetUsersResponseModel {
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
}
