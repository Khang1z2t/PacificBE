package com.pacific.pacificbe.controller.admin;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.VoucherRequest;
import com.pacific.pacificbe.dto.response.VoucherResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.services.VoucherService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.ADMIN_VOUCHER)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminVoucherController {
    private final VoucherService voucherService;

    @GetMapping(UrlMapping.GET_ALL_VOUCHERS)
    @Operation(summary = "Lấy danh sách tất cả voucher")
    ResponseEntity<ApiResponse<List<VoucherResponse>>> getAllVouchers() {
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Lấy danh sách thành công", voucherService.getAllVouchers())
        );
    }

    @GetMapping(UrlMapping.GET_VOUCHER_BY_ID)
    @Operation(summary = "Lấy thông tin voucher theo ID")
    public ResponseEntity<ApiResponse<VoucherResponse>> getVoucherById(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Lấy thông tin thành công", voucherService.getVoucherById(id)));
    }

    @GetMapping(UrlMapping.GET_VOUCHER_BY_CODE)
    @Operation(summary = "Lấy thông tin voucher theo mã")
    public ResponseEntity<ApiResponse<VoucherResponse>> getVoucherByCode(
            @RequestParam String codeVoucher) {

        return voucherService.getVoucherByCode(codeVoucher)
                .map(voucher -> ResponseEntity.ok(new ApiResponse<>(200, "Lấy thành công", voucher)))
                .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));
    }

    @PostMapping(UrlMapping.CREATE_VOUCHER)
    @Operation(summary = "Thêm voucher")
    public ResponseEntity<ApiResponse<VoucherResponse>> createVoucher(@RequestBody VoucherRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Thêm thành công", voucherService.createVoucher(request)));
    }

    @PutMapping(UrlMapping.UPDATE_VOUCHER)
    @Operation(summary = "Cập nhật voucher")
    public ResponseEntity<ApiResponse<VoucherResponse>> updateVoucher(@PathVariable String id, @RequestBody VoucherRequest request) {
        System.out.println("Updating voucher with ID: " + id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật thành công", voucherService.updateVoucher(id, request)));
    }

    @PatchMapping(UrlMapping.UPDATE_STATUS_VOUCHER)
    @Operation(summary = "Cập nhật trạng thái voucher")
    public ResponseEntity<ApiResponse<VoucherResponse>> updateStatusVoucher(
            @PathVariable String id,
            @RequestBody UpdateStatusVoucherRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Cập nhật trạng thái thành công", voucherService.updateStatus(id, request)));
    }

    @DeleteMapping(UrlMapping.DELETE_VOUCHER)
    @Operation(summary = "Xóa voucher")
    public ResponseEntity<ApiResponse<String>> deleteVoucher(@PathVariable String id) {
        voucherService.deleteVoucher(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Xóa thành công", "Voucher đã được xóa"));
    }

}
