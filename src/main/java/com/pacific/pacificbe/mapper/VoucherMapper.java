package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.VoucherResponse;
import com.pacific.pacificbe.model.Voucher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherMapper {
    ModelMapper modelMapper;

    public Voucher toVoucher(VoucherResponse voucherResponse) {
        return modelMapper.map(voucherResponse, Voucher.class);
    }

    public List<VoucherResponse> toVoucherResponseList(List<Voucher> vouchers) {
        return vouchers.stream()
                .map(this::toResponse)
                .toList();
    }

    public VoucherResponse toResponse(Voucher voucher) {
        return modelMapper.map(voucher, VoucherResponse.class);
    }
}
