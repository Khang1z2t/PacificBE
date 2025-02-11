package com.pacific.pacificbe.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pacific.pacificbe.model.Tours;
import com.pacific.pacificbe.repository.TourRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourService {
    private final TourRepository toursRepository;

    public List<Tours> getAllTours() {
        return toursRepository.findAll();
    }
    
    public Tours getTourById(Long id) {
        return toursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tour không tồn tại với ID: " + id));
    }
}