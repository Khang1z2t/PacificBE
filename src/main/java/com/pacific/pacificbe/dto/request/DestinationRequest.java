package com.pacific.pacificbe.dto.request;

import lombok.Data;

@Data
public class DestinationRequest {
    private String city;
    private String country;
    private String fullAddress;
    private String name;
    private String region;
}
