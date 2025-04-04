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
        transport.setTypeTransport(request.getTypeTransport());
        transport.setCost(request.getCost());
        transport.setImageURL(request.getImageURL());
        transport.setActive(request.isActive());
        return transport;
    }

    public TransportResponse toResponse(Transport transport) {
        TransportResponse response = new TransportResponse();
        response.setId(transport.getId());
        response.setName(transport.getName());
        response.setTypeTransport(transport.getTypeTransport());
        response.setCost(transport.getCost());
        response.setImageURL(transport.getImageURL());
        response.setActive(transport.isActive());
        return response;
    }
}
