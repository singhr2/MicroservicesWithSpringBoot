package com.github.singhr2.api.external.model;

import lombok.*;

import java.util.UUID;

@Getter @Setter
@ToString
@NoArgsConstructor @AllArgsConstructor
public class SampleServiceResponseModel {
    private UUID id;
    private String name;
    private String emailID;
    private Long mobileNumber;
    private String depttID;
}
