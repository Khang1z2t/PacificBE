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
public class CreateTourRequest {
    @Schema(description = "Tên tour", example = "Tour Hồ Chí Minh 3 ngày 2 đêm")
    String title;
    @Schema(description = "Điểm xuất phát - diểm kết thúc, Mô tả", example = "Hồ Chí Minh đến Vũng Tàu, ....")
    String description;
    @Schema(description = "Độ dài tour", example = "3")
    Integer duration;
    @Schema(description = "Id của category", example = "8ab68a08-4d28-43f7-9b37-61bfdc02cd48")
    String categoryId;
    @Schema(description = "Id của điểm đến")
    String destinationId;
}
