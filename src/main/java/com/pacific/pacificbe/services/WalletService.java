package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.refundFunction.RefundRequestDTO;
import com.pacific.pacificbe.dto.response.BookingResponse;
import com.pacific.pacificbe.dto.response.refundFunction.*;

import java.math.BigDecimal;
import java.util.List;

public interface WalletService {

    BookingResponse refund(RefundRequestDTO request);
    void approveRefund(ApproveRefundRequestDto request);
    void deposit(BigDecimal amount);
    void withdraw(BigDecimal amount);
    List<TransactionResponseDto> getTransactions(String userId);
    BalanceResponseDto getBalance(String id, String type);
    List<RefundRequestResponseDto> getRefundRequests();
    public SystemBalanceResponseDto getSystemBalance();

    void depositSystemWallet(BigDecimal amount);
    void withdrawSystemWallet(BigDecimal amount);
}
