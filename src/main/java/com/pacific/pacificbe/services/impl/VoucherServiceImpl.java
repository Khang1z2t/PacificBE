package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.VoucherRequest;
import com.pacific.pacificbe.dto.response.VoucherCodeResponse;
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
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        voucher.setDeleteAt(LocalDateTime.now());
        voucher.setStatus(VoucherStatus.INACTIVE.toString());
        voucherRepository.save(voucher);
    }

    @Override
    public VoucherResponse updateStatus(String id, String status) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        if (EnumUtils.isValidEnum(VoucherStatus.class, status.toUpperCase())) {
            voucher.setStatus(status.toUpperCase());
        } else {
            throw new AppException(ErrorCode.INVALID_VOUCHER_STATUS);
        }
        voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponse(voucher);
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
        voucher.setFirstTimeUserOnly(request.getFirstTimeUserOnly());
        voucher.setMaxDiscountAmount(request.getMaxDiscountAmount());

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
//    public Optional<VoucherResponse> getVoucherByCode(String codeVoucher) {
//        String userId = AuthUtils.getCurrentUserId();
//        var user = userRepository.findById(userId).orElseThrow(
//                () -> new AppException(ErrorCode.USER_NOT_FOUND));
//        var voucher = voucherRepository.findByCodeVoucher(codeVoucher).orElse(null);
//        if (voucher != null) {
//            LocalDateTime now = LocalDateTime.now();
//            if (voucher.getStartDate().isAfter(now) || voucher.getEndDate().isBefore(now)) {
//                return Optional.empty();
//            }
//            if (voucher.getQuantity() < 0) {
//                return Optional.empty();
//            }
//            if (!voucher.getStatus().equals(VoucherStatus.ACTIVE.toString())) {
//                return Optional.empty();
//            }
//            if (voucher.getMinOrderValue().compareTo(BigDecimal.ZERO) > 0) {
//                return Optional.empty();
//            }
//
//            long userVoucherUsage = bookingRepository.countByUserIdAndVoucherId(user.getId(), voucher.getId());
//            if (voucher.getUserLimit() != null && userVoucherUsage >= voucher.getUserLimit()) {
//                return Optional.empty();
//            }
//        }
//        return Optional.ofNullable(voucherMapper.toVoucherResponse(voucher));
//    }
    public VoucherResponse getVoucherByCode(String codeVoucher) {
        String userId = AuthUtils.getCurrentUserId();
        var user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        Voucher voucher = voucherRepository.findByCodeVoucher(codeVoucher).orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
        return voucherMapper.toVoucherResponse(voucher);
    }

    @Override
    public Boolean checkVoucher(String codeVoucher, BigDecimal orderValue, String tourId) {
        String userId = AuthUtils.getCurrentUserId();
        var user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        var voucher = voucherRepository.findByCodeVoucher(codeVoucher).orElse(null);

        if (voucher != null) {
            LocalDateTime now = LocalDateTime.now();

            if (!voucher.isActive()) {
                return false;
            }

            // Kiểm tra thời gian hiệu lực
            if (voucher.getStartDate().isAfter(now) || voucher.getEndDate().isBefore(now)) {
                return false;
            }

            // Kiểm tra số lượng voucher
            if (voucher.getQuantity() < 0) {
                return false;
            }

            // Kiểm tra trạng thái voucher
            if (!voucher.getStatus().equals(VoucherStatus.ACTIVE.toString())) {
                return false;
            }

            // Kiểm tra giá trị đơn hàng tối thiểu
            if (voucher.getMinOrderValue().compareTo(orderValue) > 0) {
                return false;
            }

            // Kiểm tra giới hạn sử dụng của user
            long userVoucherUsage = bookingRepository.countByUserIdAndVoucherId(user.getId(), voucher.getId());
            if (voucher.getUserLimit() != null && userVoucherUsage >= voucher.getUserLimit()) {
                return false;
            }

            // Kiểm tra chỉ dành cho người dùng mới
            if (voucher.getFirstTimeUserOnly()) {
                long userBookingCount = bookingRepository.countByUser(user);
                if (userBookingCount > 0) {
                    return false;
                }
            }

            // Kiểm tra áp dụng cho tour hoặc category
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

            return true; // Voucher hợp lệ
        }

        return false; // Không tìm thấy voucher
    }

    @Override
    public VoucherCodeResponse checkVoucherCode(String codeVoucher, BigDecimal orderValue, String tourId) {
        var isValid = checkVoucher(codeVoucher, orderValue, tourId);
        BigDecimal discountValue = BigDecimal.ZERO;
        String code = null;
        if (isValid) {
            var voucher = voucherRepository.findByCodeVoucher(codeVoucher)
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
            discountValue = voucher.getDiscountValue();
            code = voucher.getCodeVoucher();
        }
        return VoucherCodeResponse.builder()
                .isValid(isValid)
                .voucherCode(code)
                .discountValue(discountValue)
                .build();
    }

    @Override
    public List<VoucherResponse> getVouchersByIds(List<String> ids) {
        List<Voucher> vouchers = voucherRepository.findAllById(ids);
        return voucherMapper.toVoucherResponseList(vouchers);
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
