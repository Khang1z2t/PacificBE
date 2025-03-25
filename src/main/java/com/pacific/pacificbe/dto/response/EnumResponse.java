package com.pacific.pacificbe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EnumResponse {
    private List<String> ageGroups;
    private List<String> genders;
    private List<String> bookingStatuses;
    private List<String> reviewStatuses;
    private List<String> supportStatuses;
    private List<String> tourDetailStatuses;
    private List<String> tourStatuses;
    private List<String> userRoles;
    private List<String> userStatuses;
    private List<String> voucherStatuses;

}
