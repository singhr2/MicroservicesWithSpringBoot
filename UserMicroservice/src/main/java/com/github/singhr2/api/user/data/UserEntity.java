package com.github.singhr2.api.user.data;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/*
 @Version: We can control versioning or concurrency using this annotation.
      @Version
      @Column(name = "version")
      private Date version;

 @OrderBy: Sort your data using @OrderBy annotation.
    @OrderBy("id asc")
    private Set employee_address;

 @Transient: Every non static and non-transient property of an entity is considered persistent,
  unless you annotate it as @Transient.
        @Transient
        Private int employeePhone;

 @Id @GeneratedValue(strategy=GenerationType.AUTO) // AUTO is the default strategy,
    During a commit the AUTO strategy uses the global number generator to generate a primary key for every new entity object.
    These generated values are unique at the database level and are never recycled


 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    The IDENTITY strategy also generates an automatic value during commit for every new entity object.
    The difference is that a separate identity generator is managed per type hierarchy,
    so generated values are unique only per type hierarchy.

 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
     @Entity
    // Step-1: Define a sequence - might also be in another class:
    @SequenceGenerator(name="seq", initialValue=1, allocationSize=100)
    public class EntityWithSequenceId {
        // Step-2:  Use the sequence that is defined above:
        @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
        @Id long id;
    }

*/

@Getter @Setter
@NoArgsConstructor  @AllArgsConstructor
@ToString
@Entity // Specifies that the class is an entity.
@Table(name = "USERS_DEMO")
public class UserEntity implements Serializable {

    //If this is not auto-generated, then try
    //File -> Settings -> Editor -> Inspections -> Java -> Serialization issues
    //Find serialization class without serialVersionUID and check it.
    private static final long serialVersionUID = 925189984688473766L;

    //added new field
    @Id //specifies the primary key of the entity.
    @GeneratedValue  //specifies the generation strategies for the values of primary keys
    //  @GeneratedValue(strategy=SEQUENCE, generator="ID_SEQ")
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastName;

    //Commented as we don't want to save password in plain text
    //private String password;

    //@Column(name = "EMAIL_ID", nullable = false, length = 150, unique = true)
    @Column(name = "EMAIL_ID", nullable = false, length = 150)
    private String emailId;

    @Column(name = "USER_ID", nullable = false, unique = true)
    private String userId;

    @Column(name = "ENCRYPTED_PASSWORD", nullable = false)
    private String encryptedPassword;
}
