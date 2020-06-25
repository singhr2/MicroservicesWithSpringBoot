package com.github.singhr2.api.user.dto;

import lombok.*;

import java.io.Serializable;

/*
    DTO will contains attributes from corresponding Model class
    + other attributes as needed during data transfer
 */

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString
public class UserDTO implements Serializable {
    /*
    Simply put, the serialVersionUID is a unique identifier for Serializable classes.
    This is used during the deserialization of an object,
    to ensure that a loaded class is compatible with the serialized object.
    If no matching class is found, an InvalidClassException is thrown. {baeldung}
     */
    private static final long serialVersionUID = 9876543210L;

    //corresponding to Model class
    private String firstName;
    private String lastName;
    private String password;
    private String emailId;

    //add other attributes as needed
    private String userId;
    private String encryptedPassword;

}
