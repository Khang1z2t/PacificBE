package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.VoucherRequest;
import com.pacific.pacificbe.dto.response.voucher.VoucherCodeResponse;
import com.pacific.pacificbe.dto.response.voucher.VoucherResponse;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {

    VoucherResponse getVoucherById(String id);

    List<VoucherResponse> getAllVouchers();

    VoucherResponse updateVoucher(String id, VoucherRequest request);

    void deleteVoucher(String id, boolean active);

    void deleteVoucherForce(String id);

    VoucherResponse updateStatus(String id, String status);

    VoucherResponse createVoucher(VoucherRequest request);

    VoucherResponse getVoucherByCode(String codeVoucher);

    Boolean checkVoucher(String codeVoucher, BigDecimal orderValue, String tourId);

    VoucherCodeResponse checkVoucherCode(String codeVoucher, BigDecimal orderValue, String tourId);

    List<VoucherResponse> getVouchersByIds(List<String> ids);
}
