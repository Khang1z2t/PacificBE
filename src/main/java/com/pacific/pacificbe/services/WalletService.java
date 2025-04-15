package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.refundFunction.DepositRequestDto;
import com.pacific.pacificbe.dto.request.refundFunction.RefundRequestDTO;
import com.pacific.pacificbe.dto.request.refundFunction.WithdrawRequestDto;
import com.pacific.pacificbe.dto.response.refundFunction.ApproveRefundRequestDto;
import com.pacific.pacificbe.dto.response.refundFunction.BalanceResponseDto;
import com.pacific.pacificbe.dto.response.refundFunction.RefundRequestResponseDto;
import com.pacific.pacificbe.dto.response.refundFunction.TransactionResponseDto;

import java.util.List;

public interface WalletService {

    void refund(RefundRequestDTO request);
    void approveRefund(ApproveRefundRequestDto request);
    void deposit(DepositRequestDto request);
    void withdraw(WithdrawRequestDto request);
    List<TransactionResponseDto> getTransactions(String userId);
    BalanceResponseDto getBalance(String id, String type);
    List<RefundRequestResponseDto> getRefundRequests();
}
