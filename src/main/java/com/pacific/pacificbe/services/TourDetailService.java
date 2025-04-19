package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.CreateTourDetailRequest;
import com.pacific.pacificbe.dto.request.UpdateTourDetailRequest;
import com.pacific.pacificbe.dto.request.UpdateTourRequest;
import com.pacific.pacificbe.dto.response.TourDetailResponse;
import com.pacific.pacificbe.dto.response.showTour.DetailTourResponse;

import java.util.List;

public interface TourDetailService {
    TourDetailResponse addTourDetail(CreateTourDetailRequest request);

    TourDetailResponse updateTourDetail(UpdateTourDetailRequest request, String tourDetailId);

    List<TourDetailResponse> getAll();

    TourDetailResponse getTourDetailById(String id);

    List<TourDetailResponse> getTourDetailByTourId(String tourId);

    List<DetailTourResponse> getTourDetailMonth(String tourId);

    List<DetailTourResponse> getTourDetailDay(String tourId, String months);

    void updateRatingAvg(String tourDetailId);
}
