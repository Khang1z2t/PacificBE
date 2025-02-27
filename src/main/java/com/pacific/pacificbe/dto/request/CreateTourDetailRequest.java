package com.pacific.pacificbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateTourDetailRequest {
    @Schema(description = "Tên tour", example = "Tour du lịch Hà Nội")
    String title;
    @Schema(description = "Mô tả tour", example = "Tour du lịch Hà Nội 3 ngày 2 đêm")
    String descriptions;
    @Schema(description = "Thời gian tour", example = "3")
    Integer duration;
    @Schema(description = "Ngày bắt đầu tour", example = "2025-12-01")
    LocalDate startDate;
    @Schema(description = "Ngày kết thúc tour", example = "2025-12-03")
    LocalDate endDate;
    @Schema(description = "Giá tour cho người lớn", example = "1000000")
    BigDecimal priceAdults;
    @Schema(description = "Giá tour cho trẻ em", example = "500000")
    BigDecimal priceChildren;
    @Schema(description = "Số lượng người", example = "10")
    Integer quantity;
    @Schema(description = "Id của tour", example = "1")
    String tourId;
    @Schema(description = "Id của combo", example = "1")
    String comboId;
    @Schema(description = "Id của khách sạn", example = "1")
    String hotelId;
    @Schema(description = "Id của phương tiện", example = "1")
    String transportId;
    @Schema(description = "Lịch trình của tour")
    List<CreateItineraryRequest> itineraries;
}
