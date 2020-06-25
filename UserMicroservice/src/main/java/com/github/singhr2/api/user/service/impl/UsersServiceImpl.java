package com.github.singhr2.api.user.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.singhr2.api.user.data.UserEntity;
import com.github.singhr2.api.user.data.UserRepository;
import com.github.singhr2.api.user.dto.UserDTO;
import com.github.singhr2.api.user.service.UsersService;
import com.netflix.discovery.converters.Auto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersServiceImpl.class);

    //@Autowired
    private UserRepository userRepository;

    //@Autowired
    //defined in PasswordEncodingUtil.java
    private PasswordEncoder bCryptPasswordEncoder;

    //constructor-based dependency injection (DI)
    @Autowired
    public UsersServiceImpl( UserRepository userRepo, PasswordEncoder bCPEncoder){
        userRepository = userRepo;
        bCryptPasswordEncoder = bCPEncoder;
    }


    @Override
    public UserDTO createUser(UserDTO userDTO) {
        LOGGER.info("====> Entered Service Impl...");
        //generate a new value
        userDTO.setUserId(UUID.randomUUID().toString());

        //Using Model mapper, map from DTO to entity class
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); //LOOSE, STANDARD, STRICT
        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);
        LOGGER.info("userEntity :" + userEntity);
        //TODO  Remove hard-coding
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode("HardcodedPwd"));

        //Save the entity object
        LOGGER.info(" Going to save the entity object - " + userEntity);
        UserEntity userEntityReturned = userRepository.save(userEntity);
        LOGGER.info(" userEntity saved. "+ userEntityReturned );

        //Map the entity object to dto to be returned to Controller class
        UserDTO userDTOReturned = mapper.map(userEntityReturned, UserDTO.class);

        return userDTOReturned;
    }
}