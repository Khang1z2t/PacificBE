package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.TourDTO;
import com.pacific.pacificbe.model.Tours;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TourMaper {
    private final ModelMapper modelMapper = new ModelMapper();

    public TourDTO toDTO(Tours tours) {
        return modelMapper.map(tours, TourDTO.class);
    }

    public Tours toEntity(TourDTO dto) {
        return modelMapper.map(dto, Tours.class);
    }
}
