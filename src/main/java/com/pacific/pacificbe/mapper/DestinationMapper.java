package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.request.DestinationRequest;
import com.pacific.pacificbe.dto.response.DestinationResponse;
import com.pacific.pacificbe.model.Destination;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DestinationMapper {
    private final ModelMapper modelMapper;

    public Destination toEntity(DestinationRequest request) {
        return modelMapper.map(request, Destination.class);
    }

    public DestinationResponse toResponse(Destination destination) {
        return modelMapper.map(destination, DestinationResponse.class);
    }
}
