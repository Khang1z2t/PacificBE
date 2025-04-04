package com.pacific.pacificbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTourDetailRequest {
    @Schema(description = "Ngày bắt đầu tour", example = "2025-12-01")
    LocalDate startDate;
    LocalTime startTime;
    @Schema(description = "Ngày kết thúc tour", example = "2025-12-03")
    LocalDateTime endDate;
    @Schema(description = "Giá tour cho người lớn", example = "1000000")
    BigDecimal priceAdults;
    @Schema(description = "Giá tour cho trẻ em", example = "500000")
    BigDecimal priceChildren;
    @Schema(description = "Số lượng người", example = "10")
    Integer quantity;
    @Schema(description = "Id của tour", example = "1")
    String tourId;
    @Schema(description = "id của hdv", example = "1")
    String guideId;
    @Schema(description = "Id của khách sạn", example = "1")
    String hotelId;
    @Schema(description = "Id của phương tiện", example = "1")
    String transportId;
    @Schema(description = "Lịch trình của tour")
    List<CreateItineraryRequest> itineraries;
}
