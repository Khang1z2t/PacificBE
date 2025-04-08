package com.pacific.pacificbe.mapper;

import com.pacific.pacificbe.dto.response.ItineraryResponse;
import com.pacific.pacificbe.model.Itinerary;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ItineraryMapper {
    public List<ItineraryResponse> toItineraryResponse(Itinerary itinerary) {
        return List.of(ItineraryResponse.builder()
                .dayNumber(itinerary.getDayNumber())
                .title(itinerary.getTitle())
                .notes(itinerary.getNotes())
                .build());
    }
}
