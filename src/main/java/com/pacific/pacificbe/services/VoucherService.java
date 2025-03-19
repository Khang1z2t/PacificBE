package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.UpdateStatusVoucherRequest;
import com.pacific.pacificbe.dto.request.VoucherRequest;
import com.pacific.pacificbe.dto.response.VoucherResponse;

import java.util.List;
import java.util.Optional;

public interface VoucherService {

    VoucherResponse getVoucherById(String id);

    List<VoucherResponse> getAllVouchers();

    VoucherResponse updateVoucher(String id, VoucherRequest request);

    VoucherResponse deleteVoucher(String id);

    VoucherResponse updateStatus(String id, UpdateStatusVoucherRequest request);

    VoucherResponse createVoucher(VoucherRequest request);

    Optional<VoucherResponse> getVoucherByCode(String codeVoucher);
}
