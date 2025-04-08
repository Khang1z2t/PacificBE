package com.pacific.pacificbe.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryDay {
    int dayNumber;
    String title;
    String notes;
}
