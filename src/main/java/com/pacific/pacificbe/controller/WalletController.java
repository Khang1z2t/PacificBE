package com.pacific.pacificbe.controller;

import com.pacific.pacificbe.dto.ApiResponse;
import com.pacific.pacificbe.dto.request.refundFunction.RefundRequestDTO;
import com.pacific.pacificbe.dto.response.refundFunction.*;
import com.pacific.pacificbe.services.WalletService;
import com.pacific.pacificbe.utils.UrlMapping;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(UrlMapping.WALLET)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletController {

    private final WalletService walletService;

    @PostMapping(UrlMapping.WALLET_REFUND)
    public ResponseEntity<String> refund(@RequestBody RefundRequestDTO request) {
        walletService.refund(request);
        return ResponseEntity.ok("Refund request submitted successfully");
    }

    @PostMapping(UrlMapping.WALLET_APPROVE_REFUND)
    public ResponseEntity<String> approveRefund(@RequestBody ApproveRefundRequestDto request) {
        walletService.approveRefund(request);
        return ResponseEntity.ok("Refund request " + (request.isApproved() ? "approved" : "rejected") + " successfully");
    }

    @PostMapping(UrlMapping.WALLET_DEPOSIT)
    public ResponseEntity<String> deposit(@RequestParam BigDecimal amount) {
        walletService.deposit(amount);
        return ResponseEntity.ok("Deposit processed successfully");
    }

    @PostMapping(UrlMapping.WALLET_WITHDRAW)
    public ResponseEntity<String> withdraw(@RequestParam BigDecimal amount) {
        walletService.withdraw(amount);
        return ResponseEntity.ok("Withdraw processed successfully");
    }

    @GetMapping(UrlMapping.WALLET_TRANSACTION)
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(@RequestParam(required = false) String userId) {
        return ResponseEntity.ok(walletService.getTransactions(userId));
    }

    @GetMapping(UrlMapping.WALLET_GET_BALANCE)
    @Operation(summary = "Lấy số dư theo user hoặc wallet")
    public ResponseEntity<BalanceResponseDto> getBalance(@RequestParam String id, @RequestParam String type) {
        return ResponseEntity.ok(walletService.getBalance(id, type));
    }

    @GetMapping(UrlMapping.REFUND_REQUESTS)
    @Operation(summary = "Lấy danh sách yêu cầu hoàn tiền")
    public ResponseEntity<ApiResponse<List<RefundRequestResponseDto>>> getRefundRequests() {
        return ResponseEntity.ok(
                ApiResponse.<List<RefundRequestResponseDto>>builder()
                        .data(walletService.getRefundRequests())
                        .message("Lấy danh sách yêu cầu hoàn tiền thành công")
                        .build());
    }

    @GetMapping(UrlMapping.WALLET_SYSTEM_BALANCE)
    @Operation(summary = "Lấy số dư hệ thống")
    public ResponseEntity<ApiResponse<SystemBalanceResponseDto>> getSystemBalance() {
        return ResponseEntity.ok(
                ApiResponse.<SystemBalanceResponseDto>builder()
                        .data(walletService.getSystemBalance())
                        .message("Lấy số dư hệ thống thành công")
                        .build());
    }
}
