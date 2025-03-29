package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.VoucherRequest;
import com.pacific.pacificbe.dto.response.VoucherResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VoucherService {

    VoucherResponse getVoucherById(String id);

    List<VoucherResponse> getAllVouchers();

    VoucherResponse updateVoucher(String id, VoucherRequest request);

    void deleteVoucher(String id);

    VoucherResponse updateStatus(String id, String status);

    VoucherResponse createVoucher(VoucherRequest request);

    Optional<VoucherResponse> getVoucherByCode(String codeVoucher);

    Boolean checkVoucherCode(String codeVoucher, BigDecimal orderValue, String tourId);
}
