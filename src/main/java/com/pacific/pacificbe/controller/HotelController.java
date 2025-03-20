package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.services.HotelService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.HOTELS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelController {
   private final HotelService hotelService;

    @Operation(summary = "Lấy danh sách khách sạn", description = "Trả về danh sách tất cả các khách sạn")
    @GetMapping(UrlMapping.GET_ALL_HOTELS)
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        List<HotelResponse> response = hotelService.getAllHotels();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lấy khách sạn theo ID", description = "Trả về thông tin của khách sạn dựa trên ID")
    @GetMapping(UrlMapping.GET_HOTEL_BY_ID)
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable String id) {
        HotelResponse response = hotelService.getHotelById(id);
        return ResponseEntity.ok(response);
    }
}
