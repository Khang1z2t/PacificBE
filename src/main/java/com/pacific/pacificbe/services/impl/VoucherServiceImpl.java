package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.UpdateStatusVoucherRequest;
import com.pacific.pacificbe.dto.request.VoucherRequest;
import com.pacific.pacificbe.dto.response.VoucherResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.VoucherMapper;
import com.pacific.pacificbe.model.Voucher;
import com.pacific.pacificbe.repository.VoucherRepository;
import com.pacific.pacificbe.services.VoucherService;
import com.pacific.pacificbe.utils.enums.VoucherStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements VoucherService {

    VoucherRepository voucherRepository;
    VoucherMapper voucherMapper;

    @Override
    public VoucherResponse getVoucherById(String id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        return voucherMapper.toResponse(voucher);
    }

    @Override
    public List<VoucherResponse> getAllVouchers() {
        return voucherRepository.findAll().stream()
                .map(voucherMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public VoucherResponse deleteVoucher(String id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        VoucherResponse response = voucherMapper.toResponse(voucher);
        voucherRepository.delete(voucher);
        log.info("Xóa voucher thành công: {}", id);
        return response;
    }

    @Override
    public VoucherResponse createVoucher(VoucherRequest request) {
        if (request.getNameVoucher() == null || request.getCodeVoucher() == null) {
            throw new IllegalArgumentException("Tên và mã voucher không được để trống!");
        }

        Voucher voucher = new Voucher();
        voucher.setNameVoucher(request.getNameVoucher());
        voucher.setCodeVoucher(request.getCodeVoucher());
        BigDecimal discount = request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO;
        voucher.setDiscount(discount);
        int quantity = request.getQuantity() != null ? request.getQuantity() : 0;
        voucher.setQuantity(quantity);
        voucher.setStatus(request.getStatus() != null ? request.getStatus() : "pending");

        // Chuyển đổi LocalDate từ request
        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate endDate = request.getEndDate() != null ? request.getEndDate() : LocalDate.now();
        voucher.setStartDate(startDate);
        voucher.setEndDate(endDate);

        // Lưu vào DB
        Voucher savedVoucher = voucherRepository.saveAndFlush(voucher);
        if (savedVoucher == null || savedVoucher.getId() == null) {
            throw new IllegalStateException("Không thể tạo voucher, dữ liệu bị null!");
        }
        log.info("Tạo voucher với id: {}", savedVoucher.getId());

        // Trả về VoucherResponse
        return new VoucherResponse(
                savedVoucher.getId(),
                savedVoucher.getNameVoucher(),
                savedVoucher.getCodeVoucher(),
                savedVoucher.getDiscount(),
                savedVoucher.getQuantity(),
                savedVoucher.getStatus(),
                savedVoucher.getStartDate(),
                savedVoucher.getEndDate()
        );
    }

    @Override
    public Optional<VoucherResponse> getVoucherByCode(String codeVoucher) {
        return voucherRepository.findByCodeVoucher(codeVoucher)
                .map(voucherMapper::toResponse);
    }

    @Override
    public VoucherResponse updateStatus(String id, UpdateStatusVoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        String newStatus = request.getStatus();
        if (newStatus != null && !newStatus.trim().isEmpty()) {
            try {
                voucher.setStatus(VoucherStatus.valueOf(newStatus.toUpperCase()).name());
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrorCode.INVALID_VOUCHER_STATUS);
            }
        } else {
            // Toggle giữa ACTIVE <-> INACTIVE
            VoucherStatus currentStatus = VoucherStatus.valueOf(voucher.getStatus().toUpperCase());
            voucher.setStatus(currentStatus == VoucherStatus.ACTIVE ? VoucherStatus.INACTIVE.name() : VoucherStatus.ACTIVE.name());
        }
        // Cập nhật DB
        voucherRepository.save(voucher);
        log.info("Updated voucher [{}] to status: {}", id, voucher.getStatus());
        return voucherMapper.toResponse(voucher);
    }

    @Override
    public VoucherResponse updateVoucher(String id, VoucherRequest request) {
        if (id == null || id.trim().isEmpty()) {
            log.warn("ID voucher không hợp lệ: null hoặc rỗng!");
            throw new AppException(ErrorCode.INVALID_ID);
        }
        log.info("Kiểm tra voucher với ID: {}", id);
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Không tìm thấy voucher với ID: {}", id);
                    return new AppException(ErrorCode.VOUCHER_NOT_FOUND);
                });
        log.info("Tìm thấy voucher: {}", voucher);

        // Cập nhật voucher
        voucher.setNameVoucher(request.getNameVoucher());
        voucher.setCodeVoucher(request.getCodeVoucher());
        voucher.setDiscount(request.getDiscount());
        voucher.setQuantity(request.getQuantity());
        voucher.setStatus(request.getStatus());

        // Kiểm tra ngày hợp lệ
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate())) {
                log.warn("Ngày kết thúc không thể nhỏ hơn ngày bắt đầu!");
                throw new AppException(ErrorCode.INVALID_DATE_RANGE);
            }
            voucher.setStartDate(request.getStartDate());
            voucher.setEndDate(request.getEndDate());
        }

        // Lưu voucher sau khi cập nhật
        Voucher updatedVoucher = voucherRepository.save(voucher);
        log.info("Cập nhật thành công voucher với ID: {}", id);
        return voucherMapper.toResponse(updatedVoucher);
    }
}
