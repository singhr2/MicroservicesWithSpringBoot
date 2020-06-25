package com.github.singhr2.api.user.service;

import com.github.singhr2.api.user.dto.UserDTO;

public interface UsersService {
    UserDTO createUser(UserDTO newUserInfo);
}
