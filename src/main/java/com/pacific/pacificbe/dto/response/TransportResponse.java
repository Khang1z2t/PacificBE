package com.pacific.pacificbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportResponse {
    String id;
    String name;
    BigDecimal cost;
    String typeTransport;
    boolean active;
}
