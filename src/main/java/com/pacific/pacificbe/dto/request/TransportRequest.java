package com.pacific.pacificbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportRequest {
    private String id;
    private String name;
    private BigDecimal cost;
    private String ImageURL;
    private String typeTransport;
    private boolean active;
}
