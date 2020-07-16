package com.github.singhr2.api.user.service;

import com.github.singhr2.api.user.dto.UserDTO;
import com.github.singhr2.api.user.model.SampleServiceResponseModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/*
  Why 'extends UserDetailsService' ?

  This interface is extending the UserDetailsService interface to retrieve user-related data.
  It has one method named loadUserByUsername() which can be overridden
  to customize the process of finding the user.
    -> Refer related changes in UserWebSecurityConfigurer and UserAuthenticationFilter
 */
public interface UsersService extends UserDetailsService {
    UserDTO createUser(UserDTO newUserInfo);

    public List<UserDTO> getAllUsers();

    //Added for User Authentication using email-id/password
    UserDTO getUserDetailsByEmailId(String emailId);

    List<SampleServiceResponseModel> callExternalService();
}
