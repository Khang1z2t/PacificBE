package com.pacific.pacificbe.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class DestinationResponse {
    private UUID id;
    private String city;
    private String country;
    private String fullAddress;
    private String name;
    private String region;
}