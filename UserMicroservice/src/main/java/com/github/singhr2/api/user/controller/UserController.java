package com.github.singhr2.api.user.controller;

import com.github.singhr2.api.user.dto.UserDTO;
import com.github.singhr2.api.user.model.CreateUserRequestModel;
import com.github.singhr2.api.user.model.CreateUserResponseModel;
import com.github.singhr2.api.user.model.GetUsersResponseModel;
import com.github.singhr2.api.user.service.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.github.singhr2.api.user.security.SecurityConstants.PROPERTY_JWT_TOKEN_SECRET;

/*
 Note: get the Hostname + Port Number from Eureka e.g.,

 http://192.168.1.5:64001/actuator
 http://192.168.1.5:54918/api/users/

 http://192.168.1.5:54918/api/users/service-instances/USER-SERVICE

 http://192.168.1.5:54918/api/users/health-check

 a) Eureka Console> Instances currently registered with Eureka > Application = Desired application name
 or
 b) spring.application.name=users-ws
 http://192.168.1.5:60606/api/users/service-instances/users-ws/
 */

@RestController
@RequestMapping("/api/users") //This is referenced in UserWebSecurity class
//TODO CHECK IF REQUIRED HERE -> @RefreshScope
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UsersService usersService;

    @Autowired
    private Environment env;

    //@Value("${property_key_name:default_value}")
    //@Value("${spring.application.name}")
    //private String appName;

    @Autowired
    private DiscoveryClient discoveryClient;
    //private EurekaClient eurekaClient;

    @GetMapping
    public ResponseEntity<List<GetUsersResponseModel>> getAllUsers(){
        List<GetUsersResponseModel> usersModel = new ArrayList<>();
        GetUsersResponseModel userModel = null;

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        List<UserDTO> usersDTO = usersService.getAllUsers();
        for(UserDTO dto : usersDTO){
            userModel = mapper.map(dto, GetUsersResponseModel.class);
            usersModel.add(userModel);
        }

        return new ResponseEntity<>(usersModel, HttpStatus.OK);
    }

    @GetMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    @GetMapping("/health-check")
    public String greeting() {
        //PROPERTY_JWT_TOKEN_SECRET added to check value refresh by spring cloud bus
        return String.format("Hello from '%s' @ Port# '%s' ! , Using token secret : '%s'",
                env.getProperty("spring.application.name"),
                env.getProperty("local.server.port"),
                env.getProperty(PROPERTY_JWT_TOKEN_SECRET));
    }


    //------------------------------------------------------------
    //{OPTION-3} This method is returning a different Model in ResponseEntity
    //------------------------------------------------------------

    // **** adding consumes and produces to handle xml as well as json ****
    // To specify the content-type in REQUEST, add HEADER :
    //          Content-Type: application/json OR application/xml
    //
    // To specify the content-type in RESPONSE, add HEADER :
    //          Accept: application/json OR application/xml
    //

    @PostMapping(
            path = "/sign-up",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel createUserRequestModel){
        LOGGER.info("---> Received request to create new User");
        LOGGER.info("createUserRequestModel :" + createUserRequestModel);

        //Map the Model to DTO class
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO userDTO = mapper.map(createUserRequestModel, UserDTO.class);
        LOGGER.info("DTO :" + userDTO);

        UserDTO createdUserDTO = usersService.createUser(userDTO);

        //Map the UserDTO (from Service layer) to CreateUserResponseModel (for View Layer)
        CreateUserResponseModel responseModel = mapper.map(createdUserDTO, CreateUserResponseModel.class);

        return new ResponseEntity<>(responseModel, HttpStatus.CREATED);
    }
}