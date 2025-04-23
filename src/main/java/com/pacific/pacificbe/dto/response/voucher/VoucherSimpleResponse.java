package com.pacific.pacificbe.dto.response.voucher;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherSimpleResponse {
    private String title;
    private String codeVoucher;
    private BigDecimal discountValue;
}
