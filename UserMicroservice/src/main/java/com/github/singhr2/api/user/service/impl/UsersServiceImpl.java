package com.github.singhr2.api.user.service.impl;

import com.github.singhr2.api.user.data.entity.UserEntity;
import com.github.singhr2.api.user.data.repository.UserRepository;
import com.github.singhr2.api.user.dto.UserDTO;
import com.github.singhr2.api.user.service.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {
    private  final Logger LOGGER = LoggerFactory.getLogger( this.getClass() );

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

        //generate a new value for UserID field in database
        userDTO.setUserId(UUID.randomUUID().toString());
        LOGGER.info("Set a generated UUID value for UserID");

        userDTO.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        LOGGER.info("password ENCODED (not encrypted) using bCryptPasswordEncoder");

        //Using Model mapper, map from DTO to entity class
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); //LOOSE, STANDARD, STRICT
        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);

        //Save the entity object
        LOGGER.info(" Going to save the entity object - " + userEntity);
        UserEntity userEntityReturned = userRepository.save(userEntity);

        LOGGER.info(" userEntity saved. "+ userEntityReturned );

        //Map the entity object to dto to be returned to Controller class
        UserDTO userDTOReturned = mapper.map(userEntityReturned, UserDTO.class);

        return userDTOReturned;
    }


    @Override
    public UserDTO getUserDetailsByEmailId(String emailIdAsUsername) {
        //we need to provide user details for authentication
        UserEntity userEntity = userRepository.findByEmailId(emailIdAsUsername);

        if(userEntity == null) throw new UsernameNotFoundException(emailIdAsUsername);

        UserDTO userDto = new ModelMapper().map(userEntity, UserDTO.class);
        LOGGER.info("UserDTO returned by getUserDetailsByEmailId() : " + userDto);

        return userDto;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> fetchedUsers = new ArrayList<>();
        UserDTO userDto;
        Iterable<UserEntity> userEntities =  userRepository.findAll();

//        ModelMapper mapper = new ModelMapper();
//        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); //LOOSE, STANDARD, STRICT

        //TODO throw error when no record found

        for(UserEntity entity: userEntities){
            userDto = new ModelMapper().map(entity, UserDTO.class);
            fetchedUsers.add(userDto);
        }

        LOGGER.info("getAllUsers() returned : " + fetchedUsers);

        return fetchedUsers;
    }


    /*
    This method is from UserDetailsService interface which is used for  User authentication.
    Spring will call this method for authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity= userRepository.findByEmailId(username);

        if(userEntity == null) throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmailId(),  // username
                userEntity.getEncryptedPassword(), // password
                true,  //enabled
                true, //accountNonExpired
                true, //credentialsNonExpired
                true, //accountNonLocked
                new ArrayList<>()); //authorities
    }

}