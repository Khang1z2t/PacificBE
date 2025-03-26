package com.pacific.pacificbe.dto.request;

import com.pacific.pacificbe.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPAYRequest {
    String orderInfo;
    int amount;
    String urlReturn;
}
