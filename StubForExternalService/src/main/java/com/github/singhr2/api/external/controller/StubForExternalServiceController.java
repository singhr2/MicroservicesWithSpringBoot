package com.github.singhr2.api.external.controller;

import com.github.singhr2.api.external.model.SampleServiceResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/external")
public class StubForExternalServiceController {

    /**
     * This is dummy method added to be invoked from Users service
     * to test service to service invocation
     * using RestTemplate
     *
     * @return
     */
    @GetMapping("/test1")
    public List<String> showAll(){
        List<String> sampleDataCollection = new ArrayList<>();
        sampleDataCollection.add("Get");
        sampleDataCollection.add("Set");
        sampleDataCollection.add("Go");

        return sampleDataCollection;
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