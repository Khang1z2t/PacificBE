package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.model.Transport;
import org.springframework.stereotype.Component;

@Component
public class TransportMapper {

    public Transport toEntity(TransportRequest request) {
        Transport transport = new Transport();
        transport.setName(request.getName());
        transport.setTypeTransport(String.valueOf(request.getTypeTransport()));
        transport.setPrice(request.getPrice());
        return transport;
    }

    public TransportResponse toResponse(Transport transport) {
        TransportResponse response = new TransportResponse();
        response.setId(transport.getId());
        response.setName(transport.getName());
        response.setTypeTransport(transport.getTypeTransport());
        response.setPrice(transport.getPrice());
        return response;
    }

    public void updateEntityFromRequest(TransportRequest request, Transport transport) {
        transport.setName(request.getName());
        transport.setTypeTransport(String.valueOf(request.getTypeTransport()));
        transport.setPrice(request.getPrice());
    }
}