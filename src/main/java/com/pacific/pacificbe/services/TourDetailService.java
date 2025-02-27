package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.CreateTourDetailRequest;
import com.pacific.pacificbe.dto.response.TourDetailResponse;

import java.util.List;

public interface TourDetailService {
    TourDetailResponse addTourDetail(CreateTourDetailRequest request);
    List<TourDetailResponse> getAll();
}
