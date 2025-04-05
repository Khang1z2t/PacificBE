package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.model.Transport;
import com.pacific.pacificbe.utils.IdUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransportMapper {

    private final ModelMapper modelMapper;
    private final IdUtil idUtil;

    public Transport toEntity(TransportRequest request) {
        Transport transport = new Transport();
        transport.setName(request.getName());
        transport.setTypeTransport(request.getTypeTransport());
        transport.setCost(request.getCost());
        return transport;
    }

    public TransportResponse toResponse(Transport transport) {
        TransportResponse response = modelMapper.map(transport, TransportResponse.class);
        response.setImage(idUtil.getIdImage(transport.getImageURL()));
        return response;
    }

    public List<TransportResponse> toResponseList(List<Transport> transports) {
        return transports.stream()
                .map(this::toResponse)
                .toList();
    }
}
