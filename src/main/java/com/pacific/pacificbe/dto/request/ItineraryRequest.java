package com.pacific.pacificbe.dto.request;

import com.pacific.pacificbe.model.Itinerary;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItineraryRequest {
    List<ItineraryDay> days;
}
