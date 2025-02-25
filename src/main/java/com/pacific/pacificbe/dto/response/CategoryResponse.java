package com.pacific.pacificbe.dto.response;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String id;
    private String status;
    private String title;
    private String type;
    // Danh sách các tour ID thuộc Category
    private List<String> tourIds;
}
