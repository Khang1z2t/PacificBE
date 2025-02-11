package com.pacific.pacificbe.services;


import com.pacific.pacificbe.dto.response.TourResponse;

import java.util.List;

public interface TourService {
    List<TourResponse> getAllTours();

    TourResponse getTourById(String id);
}