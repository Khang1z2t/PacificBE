package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.mapper.TransportMapper;
import com.pacific.pacificbe.model.Transport;
import com.pacific.pacificbe.repository.TransportRepository;
import com.pacific.pacificbe.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransportImpl implements TransportService {
    private final TransportRepository transportRepository;
    private final TransportMapper transportMapper;

    @Override
    public List<TransportResponse> getAllTransports() {
        return transportRepository.findAll().stream()
                .map(transportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransportResponse getTransportById(String id) {
        Transport transport = transportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transport not found"));
        return transportMapper.toResponse(transport);
    }

    @Override
    public TransportResponse addTransport(TransportRequest request) {
        Transport transport = new Transport();
        transport.setName(request.getName());
        transport.setCost(request.getCost());
        transport.setTypeTransport(request.getTypeTransport());
        transport.setImageURL(request.getImageURL());
        transport.setActive(request.isActive());
        transport = transportRepository.save(transport);
        return transportMapper.toResponse(transport);
    }

    @Override
    public TransportResponse updateTransport(String id, TransportRequest request) {
        Transport transport = transportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transport not found"));
        transport.setName(request.getName());
        transport.setCost(request.getCost());
        transport.setTypeTransport(request.getTypeTransport());
        transport.setImageURL(request.getImageURL());
        transport.setActive(request.isActive());
        transport = transportRepository.save(transport);
        return transportMapper.toResponse(transport);
    }

    @Override
    public boolean deleteTransport(String id) {
        Transport transport = transportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transport not found"));

        // Đánh dấu đã xóa thay vì xóa khỏi database
        transport.setDeleteAt(LocalDateTime.now());
        transport.setActive(false);
        transportRepository.save(transport);
        return true;
    }

    @Override
    public TransportResponse updateTransportStatus(String id) {
        Transport transport = transportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transport not found"));

        // Thay đổi trạng thái phương tiện (ví dụ: bật/tắt trạng thái hoạt động)
        transport.setActive(!transport.isActive());
        transport = transportRepository.save(transport);
        return transportMapper.toResponse(transport);
    }
}