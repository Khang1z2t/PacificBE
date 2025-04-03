package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.TransportRequest;
import com.pacific.pacificbe.dto.response.TransportResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.TransportMapper;
import com.pacific.pacificbe.model.Image;
import com.pacific.pacificbe.model.Transport;
import com.pacific.pacificbe.repository.ImageRepository;
import com.pacific.pacificbe.repository.TransportRepository;
import com.pacific.pacificbe.services.GoogleDriveService;
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
    private final ImageRepository imageRepository;

    @Override
    public List<TransportResponse> getAllTransports() {
        return transportRepository.findAllByDeleteAtIsNull().stream()
                .map(transportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TransportResponse getTransportById(String id) {
        Transport transport = transportRepository.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSPORT_NOT_FOUND));
        return transportMapper.toResponse(transport);
    }

    @Override
    public TransportResponse addTransport(TransportRequest request) {
        Transport transport = buildTransportFromRequest(request, null);
        transport.setActive(true); // Mặc định active là true khi tạo mới
        transport = transportRepository.save(transport);
        return transportMapper.toResponse(transport);
    }

    @Override
    public TransportResponse addTransport(TransportRequest request, MultipartFile image) {
        String imageUrl = uploadImage(image);
        Transport transport = buildTransportFromRequest(request, imageUrl);
        transport.setActive(true); // Mặc định active là true khi tạo mới
        transport = transportRepository.save(transport);

        // Lưu thông tin ảnh vào bảng Image nếu có
        if (imageUrl != null) {
//            transport.setImageURL();
        }

        return transportMapper.toResponse(transport);
    }

    @Override
    public TransportResponse updateTransport(String id, TransportRequest request) {
        Transport transport = transportRepository.findByIdAndDeleteAtIsNull(id)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSPORT_NOT_FOUND));

        transport.setName(request.getName());
        transport.setCost(request.getCost());
        transport.setTypeTransport(request.getTypeTransport());
        transport.setActive(request.isActive());

        // Chỉ cập nhật imageURL nếu có giá trị mới
        if (request.getImageURL() != null && !request.getImageURL().isEmpty()) {
            transport.setImageURL(request.getImageURL());
        }

        transport = transportRepository.save(transport);
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
        return googleDriveService.uploadImageToDrive(file, FolderType.TRANSPORTS);
    }

    private Transport buildTransportFromRequest(TransportRequest request, String imageUrl) {
        return Transport.builder()
                .name(request.getName())
                .cost(request.getCost())
                .typeTransport(request.getTypeTransport())
                .active(request.isActive())
                .imageURL(imageUrl)
                .build();
    }
}