package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;

import java.util.List;

public interface TransportService {
    List<TransportResponse> getAllTransports();
    TransportResponse getTransportById(String id);
    TransportResponse addTransport(TransportRequest request);
    TransportResponse updateTransport(String id, TransportRequest request);
    boolean deleteTransport(String id);
}