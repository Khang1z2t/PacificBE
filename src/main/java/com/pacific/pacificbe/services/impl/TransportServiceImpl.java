package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TransportMapper;
import com.pacific.pacificbe.model.Transport;
import com.pacific.pacificbe.repository.TransportRepository;
import com.pacific.pacificbe.services.GoogleDriveService;
import com.pacific.pacificbe.services.SupabaseService;
import com.pacific.pacificbe.services.TransportService;
import com.pacific.pacificbe.utils.enums.FolderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransportServiceImpl implements TransportService {
    private final TransportRepository transportRepository;
    private final TransportMapper transportMapper;
    private final GoogleDriveService googleDriveService;
    private final SupabaseService supabaseService;

    @Override
    public List<TransportResponse> getAllTransports() {
        var transports = transportRepository.findAll();
        return transportMapper.toResponseList(transports);
    }

    @Override
    public TransportResponse getTransportById(String id) {
        Transport transport = transportRepository.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSPORT_NOT_FOUND));
        return transportMapper.toResponse(transport);
    }

    @Override
    public TransportResponse addTransport(TransportRequest request, MultipartFile image) {
        Transport transport = new Transport();
        return getTransportResponse(request, image, transport);
    }

    @Override
    public TransportResponse updateTransport(String id, TransportRequest request, MultipartFile image) {
        Transport transport = transportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSPORT_NOT_FOUND));
        return getTransportResponse(request, image, transport);
    }

    private TransportResponse getTransportResponse(TransportRequest request, MultipartFile image, Transport transport) {
        transport.setName(request.getName());
        transport.setCost(request.getCost());
        transport.setTypeTransport(request.getTypeTransport());

        if (image != null && !image.isEmpty()) {
            String imageUrl = supabaseService.uploadImage(image, FolderType.TRANSPORT, true);
            transport.setImageURL(imageUrl);
        }
        transportRepository.save(transport);
        return transportMapper.toResponse(transport);
    }

    @Override
    public boolean deleteTransport(String id) {
        Transport transport = transportRepository.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSPORT_NOT_FOUND));

        transport.setDeleteAt(LocalDateTime.now());
        transport.setActive(false);
        transportRepository.save(transport);
        return true;
    }

    @Override
    public TransportResponse updateTransportStatus(String id) {
        Transport transport = transportRepository.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSPORT_NOT_FOUND));

        transport.setActive(!transport.isActive());
        transport = transportRepository.save(transport);
        return transportMapper.toResponse(transport);
    }

    @Override
    public TransportResponse addTransportImage(String id, MultipartFile image) {
        Transport transport = transportRepository.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSPORT_NOT_FOUND));

        String imageUrl = uploadImage(image);

        // Cập nhật imageURL chính của transport (nếu cần)
        if (transport.getImageURL() == null || transport.getImageURL().isEmpty()) {
            transport.setImageURL(imageUrl);
            transport = transportRepository.save(transport);
        }

        return transportMapper.toResponse(transport);
    }

    private String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return supabaseService.uploadImage(file, FolderType.TRANSPORT, true);
    }

    private Transport buildTransportFromRequest(TransportRequest request, String imageUrl) {
        return Transport.builder()
                .name(request.getName())
                .cost(request.getCost())
                .typeTransport(request.getTypeTransport())
                .imageURL(imageUrl)
                .build();
    }
}