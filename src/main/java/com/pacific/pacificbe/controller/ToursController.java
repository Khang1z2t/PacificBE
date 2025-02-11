package com.pacific.pacificbe.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pacific.pacificbe.dto.TourDTO;
import com.pacific.pacificbe.mapper.TourMaper;
import com.pacific.pacificbe.services.TourService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tours")
@RequiredArgsConstructor
public class ToursController {
    private final TourService toursService;
    private final TourMaper toursMapper;

    @GetMapping("/all")
    public ResponseEntity<List<TourDTO>> getAllTours() {
        List<TourDTO> toursList = toursService.getAllTours()
                .stream()
                .map(toursMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(toursList);
    }
}
