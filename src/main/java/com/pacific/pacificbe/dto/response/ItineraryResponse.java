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
    String itineraryId;
    String dayDetail;
    String dayNumber;
    String notes;
    String title;
    String tourDetailId;

    Boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deleteAt;
}
