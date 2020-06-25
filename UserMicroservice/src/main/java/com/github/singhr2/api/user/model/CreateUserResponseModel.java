package com.github.singhr2.api.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
This response class was created to filter the response fields that will be sent to VIEW layer
e..g,  We don't want to send the password or other confidential information to UI layer
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateUserResponseModel {
    private String firstName;
    private String lastName;
    private String emailId;
    private String userId;
}
