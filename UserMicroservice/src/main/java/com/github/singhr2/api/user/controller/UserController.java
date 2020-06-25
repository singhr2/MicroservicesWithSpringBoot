package com.github.singhr2.api.user.controller;

import com.github.singhr2.api.user.dto.UserDTO;
import com.github.singhr2.api.user.model.CreateUserRequestModel;
import com.github.singhr2.api.user.model.CreateUserResponseModel;
import com.github.singhr2.api.user.service.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/*
 Note: get the Hostname + Port Number from Eureka e.g.,

 http://192.168.1.5:64001/actuator
 http://192.168.1.5:54918/api/users/

 http://192.168.1.5:54918/api/users/service-instances/USER-SERVICE

 http://192.168.1.5:54918/api/users/test

 a) Eureka Console> Instances currently registered with Eureka > Application = Desired application name
 or
 b) spring.application.name=User-Service
 http://192.168.1.5:60606/api/users/service-instances/USER-SERVICE/
 */
@RestController
@RequestMapping("/api/users") //This is referenced in UserWebSecurity class
public class UserController {
    @Autowired
    UsersService usersService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private static List<CreateUserRequestModel> users = new ArrayList<>();
    static {
        // TODO This dummy data should be replaced (in GET request handler) by a call to db to get user details.
        users.add(new CreateUserRequestModel("Ranbir", "Singh", "#123$5^7", "mailme@ranbir.com"));
        users.add(new CreateUserRequestModel("Shreya", "Patyal", "%23!(R3", "email@shreya.com"));
        users.add(new CreateUserRequestModel("Anshi", "Patyal", "*7eh3$0", "no-reply@anshi.com"));
    }

    //@Value("${property_key_name:default_value}")
    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private DiscoveryClient discoveryClient;
    //private EurekaClient eurekaClient;

    @GetMapping("/")
    public List<CreateUserRequestModel> getAllUsers(){
        return users;
    }

    @GetMapping("/service-instances/{applicationName}")
    public List<ServiceInstance> serviceInstancesByApplicationName(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    //reading from bootstrap.properties
    @GetMapping("/test")
    public String greeting() {
        return String.format("Hello from '%s'!", appName);
    }

/*
    //------------------------------------------------------------
    //{OPTION-1} This method was returning String instead of ResponseEntity.
    //------------------------------------------------------------

    @PostMapping
    public String createUser(@Valid @RequestBody UserModel userModelDetails){
        String response= "-";
        LOGGER.info("---> Received request to create new User");
        LOGGER.info("Model :" + userModelDetails);
        //Map the Model to DTO class
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO userDTO = mapper.map(userModelDetails, UserDTO.class);
        LOGGER.info("DTO :" + userDTO);

        usersService.createUser(userDTO);

        response = "User created successfully.";

        return response;
    }
*/

/*
    //------------------------------------------------------------
    //{OPTION-2} This method is used to return ResponseEntity instead of String
    //------------------------------------------------------------

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserModel userModelDetails){
        String methodResponse= "-";

        LOGGER.info("---> Received request to create new User");
        LOGGER.info("Model :" + userModelDetails);

        //Map the Model to DTO class
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO userDTO = mapper.map(userModelDetails, UserDTO.class);
        LOGGER.info("DTO :" + userDTO);

        usersService.createUser(userDTO);

        methodResponse = "User created successfully.";

        return new ResponseEntity<>(methodResponse, HttpStatus.CREATED);
    }
*/

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
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE }
    )
    //@PostMapping
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