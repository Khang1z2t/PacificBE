package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TransportService {
    List<TransportResponse> getAllTransports();

    TransportResponse getTransportById(String id);

    TransportResponse addTransport(TransportRequest request, MultipartFile image);

    TransportResponse updateTransport(String id, TransportRequest request, MultipartFile image);

    boolean deleteTransport(String id);

    TransportResponse updateTransportStatus(String id);

    TransportResponse addTransportImage(String id, MultipartFile image);
}