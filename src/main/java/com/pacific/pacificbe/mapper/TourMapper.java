package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.TourDTO;
import com.pacific.pacificbe.model.Tours;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TourMapper {
    private final ModelMapper modelMapper;

    public TourDTO toDTO(Tours tours) {
        return modelMapper.map(tours, TourDTO.class);
    }

    public Tours toEntity(TourDTO dto) {
        return modelMapper.map(dto, Tours.class);
    }
}
