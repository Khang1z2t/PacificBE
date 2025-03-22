package com.pacific.pacificbe.dto.response.showTour;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TourDateResponse {
    String id;
    String title;
    String description;
    Integer duration;
    String status;
    String thumbnail;
    Boolean available;

    String category;
    String destination;

    Boolean active;
    Date createdAt;
    Date updatedAt;
    Date deletedAt;
}
