package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.repository.TransportRepository;
import com.pacific.pacificbe.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {
    private final TransportRepository transportRepository;

    @Override
    public List<TransportResponse> getAllTransports() {
        return transportRepository.findAll();
    }

    @Override
    public TransportResponse getTransportById(String id) {
        return transportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phương tiện với ID: " + id));
    }

    @Override
    public TransportResponse addTransport(TransportRequest request) {
        return transportRepository.save(new TransportResponse(null, request.getName(),
                request.getCost(), request.getImageURL(), request.getTypeTransport(), request.isActive(),
                null, null, null));
    }

    @Override
    public TransportResponse updateTransport(String id, TransportRequest request) {
        TransportResponse transport = getTransportById(id);
        transport.setName(request.getName());
        transport.setCost(request.getCost());
        transport.setImageURL(request.getImageURL());
        transport.setTypeTransport(request.getTypeTransport());
        transport.setActive(request.isActive());
        return transportRepository.save(transport);
    }

    @Override
    public boolean deleteTransport(String id) {
        if (!transportRepository.existsById(id)) {
            throw new IllegalArgumentException("Transport ID không tồn tại!");
        }
        transportRepository.deleteById(id);
        return true;
    }

    @Override
    public TransportResponse updateTransportStatus(String id) {
        TransportResponse transport = getTransportById(id);
        transport.setActive(!transport.isActive());
        return transportRepository.save(transport);
    }
}
