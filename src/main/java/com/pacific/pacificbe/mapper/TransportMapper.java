package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.model.Transport;
import org.springframework.stereotype.Component;

@Component
public class TransportMapper {

    public Transport toEntity(Transport request) {
        Transport transport = new Transport();
        transport.setName(request.getName());
        transport.setTypeTransport(request.getTypeTransport());
        transport.setCost(request.getCost());
        return transport;
    }

    public TransportResponse toResponse(Transport transport) {
        TransportResponse response = new TransportResponse();
        response.setId(transport.getId());
        response.setName(transport.getName());
        response.setTypeTransport(transport.getTypeTransport());
        response.setCost(transport.getCost());
        return response;
    }
}
