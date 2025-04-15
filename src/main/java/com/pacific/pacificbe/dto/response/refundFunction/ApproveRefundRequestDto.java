package com.pacific.pacificbe.dto.response.refundFunction;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApproveRefundRequestDto {
    private String bookingId; // ID của booking
    private boolean approved; // true: duyệt, false: từ chối
    private String adminId; // ID của admin xử lý (tùy chọn, để ghi log)
}
