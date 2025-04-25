package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DestinationResponse {
    private String id;
    private String city;
    private String country;
    private String fullAddress;
    private String name;
    private String region;
}