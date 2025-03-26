package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.model.Hotel;
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
    HotelService hotelService;

    @Operation(summary = "Lấy danh sách khách sạn", description = "Trả về danh sách tất cả các khách sạn")
    @GetMapping(UrlMapping.GET_ALL_HOTELS)
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @Operation(summary = "Lấy khách sạn theo ID", description = "Trả về thông tin của khách sạn dựa trên ID")
    @GetMapping(UrlMapping.GET_HOTEL_BY_ID)
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable String id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @Operation(summary = "Thêm khách sạn", description = "Thêm mới một khách sạn vào hệ thống")
    @PostMapping(UrlMapping.ADD_HOTEL)
    public ResponseEntity<HotelResponse> createHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.createHotel(hotel));
    }

    @Operation(summary = "Cập nhật khách sạn", description = "Cập nhật thông tin khách sạn theo ID")
    @PutMapping(UrlMapping.UPDATE_HOTEL)
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable String id, @RequestBody Hotel hotel) {
        return ResponseEntity.ok(hotelService.updateHotel(id, hotel));
    }

    @Operation(summary = "Xóa khách sạn", description = "Xóa một khách sạn theo ID")
    @DeleteMapping(UrlMapping.DELETE_HOTEL)
    public ResponseEntity<Void> deleteHotel(@PathVariable String id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }
}
