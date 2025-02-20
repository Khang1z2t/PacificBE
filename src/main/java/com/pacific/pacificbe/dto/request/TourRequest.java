package com.pacific.pacificbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourRequest {
    @Schema(description = "Tên tour", example = "Tour Hồ Chí Minh 3 ngày 2 đêm")
    String title;
    @Schema(description = "Điểm xuất phát - diểm kết thúc, Mô tả", example = "Hồ Chí Minh đến Vũng Tàu, ....")
    String description;
    @Schema(description = "Mô tả tour", example = "3")
    int duration;
    @Schema(description = "Giá tour cho người lớn", example = "1000000")
    BigDecimal priceAdults;
    @Schema(description = "Giá tour cho trẻ em", example = "500000")
    BigDecimal priceChildren;
    @Schema(description = "Status: DRAFT, PUBLISHED, UNAVAILABLE, CANCELED", example = "DRAFT")
    String status;
    @Schema(description = "Id của category", example = "00f6cdb9-d8d5-4eb5-84c2-694ca90dd211")
    String categoryId;
    @Schema(description = "Id của guide")
    String guideId;
}
