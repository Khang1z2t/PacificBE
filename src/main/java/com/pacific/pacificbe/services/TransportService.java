package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.model.Transport;

import java.util.List;

public interface TransportService {
    List<TransportResponse> getAllTransports();
    TransportResponse getTransportById(String id);
    TransportResponse saveTransport(Transport transport);
    void deleteTransport(String id);
}
