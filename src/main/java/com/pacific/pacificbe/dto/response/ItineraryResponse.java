package com.pacific.pacificbe.dto.response;

import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItineraryResponse {
    String id;
    int dayNumber;
    String title;
    String notes;
    String tourId;
    private boolean active;
}
