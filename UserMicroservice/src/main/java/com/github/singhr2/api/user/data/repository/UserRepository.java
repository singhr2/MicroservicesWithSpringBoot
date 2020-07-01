package com.github.singhr2.api.user.data.repository;

import com.github.singhr2.api.user.data.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

/*
  We are using custom authenticationFilter for user authentication.
  so, service interface is extending UserDetailsService interface
    => loadUserByUsername() method overloaded
        => Need a method to validate the credentials against database table using email-id
          (which is used as username)
 */

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    //findBy<AttributeNameInEntityClass> will be handled by Spring
    UserEntity findByEmailId(String emailId);
}
