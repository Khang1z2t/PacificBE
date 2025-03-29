package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.VoucherRequest;
import com.pacific.pacificbe.dto.response.VoucherResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.VoucherMapper;
import com.pacific.pacificbe.model.Voucher;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.VoucherService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.enums.ApplyTo;
import com.pacific.pacificbe.utils.enums.VoucherStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements VoucherService {

    VoucherRepository voucherRepository;
    VoucherMapper voucherMapper;
    private final TourRepository tourRepository;
    private final IdUtil idUtil;
    private final CategoryRepository categoryRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    public VoucherResponse getVoucherById(String id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        return voucherMapper.toVoucherResponse(voucher);
    }

    @Override
    public List<VoucherResponse> getAllVouchers() {
        var vouchers = voucherRepository.findAll();
        return voucherMapper.toVoucherResponseList(vouchers);
    }

    @Override
    public VoucherResponse updateVoucher(String id, VoucherRequest request) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        voucher.setTitle(ObjectUtils.defaultIfNull(request.getTitle(), voucher.getTitle()));
        voucher.setCodeVoucher(ObjectUtils.defaultIfNull(request.getCodeVoucher(), voucher.getCodeVoucher()));
        voucher.setDiscountValue(ObjectUtils.defaultIfNull(request.getDiscountValue(), voucher.getDiscountValue()));
        voucher.setQuantity(ObjectUtils.defaultIfNull(request.getQuantity(), voucher.getQuantity()));
        voucher.setUserLimit(ObjectUtils.defaultIfNull(request.getUserLimit(), voucher.getUserLimit()));
        voucher.setStartDate(ObjectUtils.defaultIfNull(request.getStartDate(), voucher.getStartDate()));
        voucher.setEndDate(ObjectUtils.defaultIfNull(request.getEndDate(), voucher.getEndDate()));
        voucher.setMinOrderValue(ObjectUtils.defaultIfNull(request.getMinOrderValue(), voucher.getMinOrderValue()));

        checkValidApplyTo(request, voucher);

        if (EnumUtils.isValidEnum(VoucherStatus.class, request.getStatus().toUpperCase())) {
            voucher.setStatus(request.getStatus().toUpperCase());
        } else {
            throw new AppException(ErrorCode.INVALID_VOUCHER_STATUS);
        }

        voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponse(voucher);
    }

    @Override
    public void deleteVoucher(String id) {

    }

    @Override
    public VoucherResponse updateStatus(String id, String status) {
        return null;
    }


    @Override
    public VoucherResponse createVoucher(VoucherRequest request) {
        Voucher voucher = new Voucher();

        if (request.getMinOrderValue() != null && request.getMinOrderValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new AppException(ErrorCode.INVALID_MIN_ORDER_VALUE);
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        voucher.setTitle(request.getTitle());
        voucher.setCodeVoucher(request.getCodeVoucher() == null
                ? idUtil.generateVoucherCode(5, 10) : request.getCodeVoucher());
        voucher.setDiscountValue(request.getDiscountValue());
        voucher.setMinOrderValue(request.getMinOrderValue());
        voucher.setQuantity(request.getQuantity());
        voucher.setUserLimit(request.getUserLimit());


        voucher.setStartDate(request.getStartDate());
        voucher.setEndDate(request.getEndDate());

        checkValidApplyTo(request, voucher);

        if (EnumUtils.isValidEnum(VoucherStatus.class, request.getStatus().toUpperCase())) {
            voucher.setStatus(request.getStatus().toUpperCase());
        } else {
            throw new AppException(ErrorCode.INVALID_VOUCHER_STATUS);
        }
        voucherRepository.save(voucher);

        return voucherMapper.toVoucherResponse(voucher);
    }


    @Override
    public Optional<VoucherResponse> getVoucherByCode(String codeVoucher) {
        return Optional.empty();
    }

    @Override
    public Boolean checkVoucherCode(String codeVoucher, BigDecimal orderValue, String tourId) {
        String userId = AuthUtils.getCurrentUserId();
        var user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        var voucher = voucherRepository.findByCodeVoucher(codeVoucher).orElse(null);

        if (voucher != null) {
            LocalDateTime now = LocalDateTime.now();
            if (voucher.getStartDate().isAfter(now) || voucher.getEndDate().isBefore(now)) {
                return false;
            }
            if (voucher.getQuantity() < 0) {
                return false;
            }
            if (!voucher.getStatus().equals(VoucherStatus.ACTIVE.toString())) {
                return false;
            }
            if (voucher.getMinOrderValue().compareTo(orderValue) > 0) {
                return false;
            }

            long userVoucherUsage = bookingRepository.countByUserIdAndVoucherId(user.getId(), voucher.getId());
            if (voucher.getUserLimit() != null && userVoucherUsage >= voucher.getUserLimit()) {
                return false;
            }

            ApplyTo applyTo = ApplyTo.valueOf(voucher.getApplyTo());
            switch (applyTo) {
                case TOUR:
                    if (voucher.getTour() == null || !voucher.getTour().getId().equals(tourId)) {
                        return false;
                    }
                    break;
                case CATEGORY:
                    var tour = tourRepository.findById(tourId).orElse(null);
                    if (tour == null || voucher.getCategory() == null ||
                            !voucher.getCategory().getId().equals(tour.getCategory().getId())) {
                        return false;
                    }
                    break;
                default:
                    break;
            }
        }

        return voucher != null;
    }

    private void checkValidApplyTo(VoucherRequest request, Voucher voucher) {
        String applyToUppercase = request.getApplyTo().toUpperCase();
        if (EnumUtils.isValidEnum(ApplyTo.class, request.getApplyTo().toUpperCase())) {
            voucher.setApplyTo(request.getApplyTo().toUpperCase());

            if (!ApplyTo.ALL.name().equals(applyToUppercase)) {
                if (ApplyTo.TOUR.name().equals(applyToUppercase)) {
                    if (request.getTourId() == null || request.getTourId().isEmpty()) {
                        throw new AppException(ErrorCode.VOUCHER_TOUR_ID_REQUIRED);
                    }
                    var tour = tourRepository.findById(request.getTourId())
                            .orElseThrow(() -> new AppException(ErrorCode.TOUR_NOT_FOUND));
                    voucher.setTour(tour);
                } else if (ApplyTo.CATEGORY.name().equals(applyToUppercase)) {
                    if (request.getCategoryId() == null || request.getCategoryId().isEmpty()) {
                        throw new AppException(ErrorCode.VOUCHER_CATEGORY_ID_REQUIRED);
                    }
                    var category = categoryRepository.findById(request.getCategoryId())
                            .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
                    voucher.setCategory(category);
                }
            } else {
                voucher.setTour(null);
                voucher.setCategory(null);
            }
        } else {
            throw new AppException(ErrorCode.INVALID_APPLY_TO);
        }
    }
}
