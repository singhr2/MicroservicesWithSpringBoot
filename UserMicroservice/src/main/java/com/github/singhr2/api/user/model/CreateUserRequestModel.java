package com.github.singhr2.api.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/*
    {Model vs Entity vs DTO }

    A model object represents data in the MVC (Model View Controller) pattern.
    -> Model Object = Something that you want to display in your frontend, let’s say your html page. so you have a “User” model object and display “user.firstName” in your html page.

    An entity object represents data in the ORM (Object Relational Mapping) pattern.
    entity classes are transactional, since they are bound to database read and write operations.
    -> Entity = Something you save to a database (table). So you might have a @Entity User, which goes to a “Users” table in the db
    -> Entity is class mapped to table.

    DTO is class mapped to "view" layer mostly.
    The main reason for using a Data Transfer Object is to batch up what would be multiple remote calls into a single one.

    ### What needed to store is entity & which needed to 'show' on web page is DTO.  ###
*/

/*
    The Jetbrains IntelliJ IDEA editor is compatible with lombok.
    Add the Lombok IntelliJ plugin to add lombok support for IntelliJ:
        Go to File > Settings > Plugins
        Click on Browse repositories...
        Search for Lombok Plugin
        Click on Install plugin
        Restart IntelliJ IDEA
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateUserRequestModel {
    @NotNull(message = "First Name is mandatory")
    @Size(min=2, message = "First name should be minimum 2 characters")
    private String firstName;

    @NotNull(message = "Last Name is mandatory")
    @Size(min=2, message = "Last name should be minimum 2 characters")
    private String lastName;

    @NotNull(message = "Password is mandatory")
    @Size(min=8, max = 12, message = "Password must be between 8 to 12 characters")
    private String password;

    @NotNull(message = "EmailID is mandatory")
    @Email
    private String emailId;
}
