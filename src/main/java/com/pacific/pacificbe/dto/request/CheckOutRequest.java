package com.pacific.pacificbe.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pacific.pacificbe.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckOutRequest {
    String vnp_TransactionNo;
    String vnp_ResponseCode;
    String vnp_Amount;
    String vnp_OrderInfo;
    String userId;
}
