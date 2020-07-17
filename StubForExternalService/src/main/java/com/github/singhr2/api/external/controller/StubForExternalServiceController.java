package com.github.singhr2.api.external.controller;

import com.github.singhr2.api.external.model.SampleServiceResponseModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/external")
public class StubForExternalServiceController {
    // Sample request: http://someIp:port/api/external/v1/greet?
    @GetMapping("/{version}/greet")
    public String greeting(@PathVariable("version") String version,
                           @RequestParam(value="username", required=false) String username) {
        return String.format("Hello %s!, the version of api is %s \n", username, version);
    }

    @GetMapping("/show-all/{dummyPathVarID}")
    public List<SampleServiceResponseModel> getAllRecords(@PathVariable("dummyPathVarID") String prmDeptID){
        List<SampleServiceResponseModel> collection = new ArrayList<>();

        collection.add(new SampleServiceResponseModel( UUID.randomUUID(), "Ranbir Singh", "dummy1@ranbir.com", 1800200300L, prmDeptID));
        collection.add(new SampleServiceResponseModel( UUID.randomUUID(), "User2 Singh", "dummy2@ranbir.com", 1800111222L,prmDeptID));
        collection.add(new SampleServiceResponseModel( UUID.randomUUID(), "Hello World", "hello.world@ranbir.com", 1800400555L, prmDeptID));

        return collection;
    }

    /*
    // Just added for reference - usage of RequestHeader

    @GetMapping(path="/", produces = "application/json")
    public Employees getEmployees
            (
                    @RequestHeader(name = "X-COM-PERSIST", required = true) String headerPersist,
                    @RequestHeader(name = "X-COM-LOCATION", defaultValue = "ASIA") String headerLocation
            )
    {
        return employeeDao.getAllEmployees();
    }
    */
}