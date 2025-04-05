package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.request.HotelRequest;
import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.response.HotelResponse;
import com.pacific.pacificbe.services.HotelService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.HOTELS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HotelController {
    private final HotelService hotelService;

    @Operation(summary = "Lấy tất cả các khách sạn", description = "Lấy danh sách tất cả các khách sạn")
    @GetMapping(UrlMapping.GET_ALL_HOTELS)
    public ResponseEntity<ApiResponse<List<HotelResponse>>> getAllHotels() {
        List<HotelResponse> hotels = hotelService.getAllHotels();
        return ResponseEntity.ok(new ApiResponse<>(200, "Đã lấy thành công tất cả các khách sạn", hotels));
    }

    @Operation(summary = "Lấy khách sạn theo ID", description = "Lấy thông tin chi tiết về khách sạn theo ID của khách sạn")
    @GetMapping(UrlMapping.GET_HOTEL_BY_ID)
    public ResponseEntity<ApiResponse<HotelResponse>> getHotelById(@PathVariable String id) {
        HotelResponse hotel = hotelService.getHotelById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Đã lấy thông tin chi tiết về khách sạn thành công", hotel));
    }

    @Operation(summary = "Tạo mới khách sạn", description = "Thêm một khách sạn mới vào hệ thống")
    @PostMapping(value = UrlMapping.ADD_HOTEL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<HotelResponse>> createHotel(@ModelAttribute HotelRequest request,
                                                                  @RequestPart(required = false) MultipartFile image) {
        HotelResponse newHotel = hotelService.createHotel(request, image);
        return ResponseEntity.ok(new ApiResponse<>(201, "Khách sạn được tạo thành công", newHotel));
    }

    @Operation(summary = "Cập nhật hình ảnh khách sạn", description = "Cập nhật hình ảnh của khách sạn")
    @PostMapping(value = UrlMapping.UPDATE_HOTEL_IMAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<HotelResponse>> updateHotelImage(@PathVariable String id, @RequestParam("image") MultipartFile image) {
        HotelResponse updatedHotel = hotelService.updateHotelImage(id, image);
        return ResponseEntity.ok(new ApiResponse<>(200, "Đã cập nhật hình ảnh khách sạn thành công", updatedHotel));
    }

    @Operation(summary = "Cập nhật khách sạn", description = "Cập nhật thông tin khách sạn")
    @PutMapping(UrlMapping.UPDATE_HOTEL)
    public ResponseEntity<ApiResponse<HotelResponse>> updateHotel(@PathVariable String id, @RequestBody HotelRequest request) {
        HotelResponse updatedHotel = hotelService.updateHotel(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật khách sạn thành công", updatedHotel));
    }

    @Operation(summary = "Xóa khách sạn", description = "Xóa khách sạn khỏi hệ thống")
    @DeleteMapping(UrlMapping.DELETE_HOTEL)
    public ResponseEntity<ApiResponse<Void>> deleteHotel(@PathVariable String id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(new ApiResponse<>(204, "Khách sạn đã được xóa thành công", null));
    }
}