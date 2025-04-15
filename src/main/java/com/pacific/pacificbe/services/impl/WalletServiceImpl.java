package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.refundFunction.DepositRequestDto;
import com.pacific.pacificbe.dto.request.refundFunction.RefundRequestDTO;
import com.pacific.pacificbe.dto.request.refundFunction.WithdrawRequestDto;
import com.pacific.pacificbe.dto.response.refundFunction.ApproveRefundRequestDto;
import com.pacific.pacificbe.dto.response.refundFunction.BalanceResponseDto;
import com.pacific.pacificbe.dto.response.refundFunction.RefundRequestResponseDto;
import com.pacific.pacificbe.dto.response.refundFunction.TransactionResponseDto;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.model.Booking;
import com.pacific.pacificbe.model.SystemWallet;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.model.WalletTransaction;
import com.pacific.pacificbe.repository.BookingRepository;
import com.pacific.pacificbe.repository.SystemWalletRepository;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.repository.WalletTransactionRepository;
import com.pacific.pacificbe.services.WalletService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.enums.BookingStatus;
import com.pacific.pacificbe.utils.enums.WalletStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletServiceImpl implements WalletService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SystemWalletRepository systemWalletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    private final String SYSTEM_WALLET_ID = "SYSTEM_WALLET"; // Replace with actual wallet ID

    @Override
    @Transactional
    public void refund(RefundRequestDTO request) {
//        Check booking

        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!"PAID".equals(booking.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }


// Cap nhat trang thai booking
        booking.setStatus(BookingStatus.REFUND_REQUESTED.toString());
        bookingRepository.save(booking);

        SystemWallet systemWallet = systemWalletRepository.findById(SYSTEM_WALLET_ID)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        WalletTransaction transaction = new WalletTransaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setWallet(systemWallet);
        transaction.setBooking(booking);
        transaction.setUser(booking.getUser());
        transaction.setAmount(booking.getTotalAmount().multiply(BigDecimal.valueOf(0.8)));
        transaction.setType(BookingStatus.REFUND_REQUESTED.toString());
        transaction.setStatus(WalletStatus.PENDING.toString());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setDescription(request.getReasons());
        walletTransactionRepository.save(transaction);
    }

    @Override
    public void approveRefund(ApproveRefundRequestDto request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!"REFUND_REQUESTED".equals(booking.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        if (request.isApproved()) {
            SystemWallet systemWallet = systemWalletRepository.findById(SYSTEM_WALLET_ID)
                    .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

            BigDecimal refundAmount = booking.getTotalAmount().multiply(BigDecimal.valueOf(0.8));

            if (systemWallet.getBalance().compareTo(refundAmount) < 0) {
                throw new AppException(ErrorCode.WALLET_NOT_ENOUGH);
            }

            User user = booking.getUser();
            user.setDeposit(user.getDeposit().add(refundAmount));
            userRepository.save(user);

            systemWallet.setBalance(systemWallet.getBalance().subtract(refundAmount));
            systemWallet.setUpdatedAt(LocalDateTime.now());
            systemWalletRepository.save(systemWallet);

            WalletTransaction transaction = walletTransactionRepository.findByBookingIdAndType(booking.getId(), BookingStatus.REFUND_REQUESTED.toString());
            transaction.setType(WalletStatus.REFUNDED.toString());
            transaction.setStatus(WalletStatus.COMPLETED.toString());
            transaction.setUpdatedAt(LocalDateTime.now());
            walletTransactionRepository.save(transaction);

            booking.setStatus(BookingStatus.CANCELLED.toString());
        } else {
            WalletTransaction transaction = walletTransactionRepository.findByBookingIdAndType(booking.getId(), "REFUND_REQUEST");
            transaction.setStatus(WalletStatus.REJECTED.toString());
            transaction.setUpdatedAt(LocalDateTime.now());
            walletTransactionRepository.save(transaction);

            booking.setStatus(BookingStatus.PENDING.toString());
        }

        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void deposit(DepositRequestDto request) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setDeposit(user.getDeposit().add(request.getAmount()));
        userRepository.save(user);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setWallet(systemWalletRepository.findById(SYSTEM_WALLET_ID).orElseThrow());
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setType(WalletStatus.DEPOSIT.toString());
        transaction.setStatus(WalletStatus.COMPLETED.toString());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setDescription("Deposit for user " + user.getId());
        walletTransactionRepository.save(transaction);
    }

    @Override
    public void withdraw(WithdrawRequestDto request) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getDeposit().compareTo(request.getAmount()) < 0) {
            throw new AppException(ErrorCode.WALLET_NOT_ENOUGH);
        }

        user.setDeposit(user.getDeposit().subtract(request.getAmount()));
        userRepository.save(user);

        WalletTransaction transaction = new WalletTransaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setWallet(systemWalletRepository.findById(SYSTEM_WALLET_ID).orElseThrow());
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setType(WalletStatus.WITHDRAW.toString());
        transaction.setStatus(WalletStatus.COMPLETED.toString());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());
        transaction.setDescription("Withdraw for user " + user.getId());
        walletTransactionRepository.save(transaction);
    }

    @Override
    public List<TransactionResponseDto> getTransactions(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<WalletTransaction> transactions = walletTransactionRepository.findAll()
                .stream()
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .collect(Collectors.toList());

        return transactions.stream().map(t -> {
            TransactionResponseDto dto = new TransactionResponseDto();
            dto.setId(t.getId());
            dto.setWalletId(t.getWallet().getId());
            dto.setBookingNo(t.getBooking().getBookingNo());
            dto.setUserId(t.getUser().getId());
            dto.setAmount(t.getAmount());
            dto.setType(t.getType());
            dto.setStatus(t.getStatus());
            dto.setCreatedAt(t.getCreatedAt().toString());
            dto.setDescription(t.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public BalanceResponseDto getBalance(String id, String type) {
        BalanceResponseDto response = new BalanceResponseDto();
        response.setId(id);

        if ("USER".equals(type)) {
            String userId = AuthUtils.getCurrentUserId();
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            response.setBalance(user.getDeposit());
        } else if ("WALLET_SYSTEM".equals(type)) {
            SystemWallet wallet = systemWalletRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_ENOUGH));
            response.setBalance(wallet.getBalance());
        } else {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        return response;
    }

    @Override
    public List<RefundRequestResponseDto> getRefundRequests() {
        List<Booking> bookings = bookingRepository.findByStatus("REFUND_REQUESTED");

        return bookings.stream().map(booking -> {
            RefundRequestResponseDto dto = new RefundRequestResponseDto();
            dto.setBookingId(booking.getId());
            dto.setBookingNo(booking.getBookingNo());

            // Lấy thông tin người dùng
            User user = booking.getUser();
            if (user != null) {
                dto.setUserId(user.getId());
                dto.setUserName(user.getFirstName() + " " + user.getLastName());
                dto.setUserEmail(user.getEmail());
            }

            // Tính số tiền hoàn lại (80% totalAmount)
            BigDecimal totalAmount = booking.getTotalAmount();
            if (totalAmount != null) {
                dto.setRefundAmount(totalAmount.multiply(BigDecimal.valueOf(0.8)));
            } else {
                dto.setRefundAmount(BigDecimal.ZERO);
            }

            // Lấy lý do từ wallet_transaction
            List<WalletTransaction> transactions = walletTransactionRepository.findByBookingId(booking.getId());
            String reason = transactions.stream()
                    .filter(t -> "REFUND_REQUESTED".equals(t.getType()))
                    .findFirst()
                    .map(WalletTransaction::getDescription)
                    .orElse("Không cung cấp lý do");

            dto.setReason(reason);
            dto.setStatus(booking.getStatus());
            dto.setCreatedAt(booking.getCreatedAt());

            return dto;
        }).collect(Collectors.toList());
    }

}
