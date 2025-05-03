package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.refundFunction.RefundRequestDTO;
import com.pacific.pacificbe.dto.response.booking.BookingResponse;
import com.pacific.pacificbe.dto.response.refundFunction.*;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.mapper.BookingMapper;
import com.pacific.pacificbe.model.*;
import com.pacific.pacificbe.repository.*;
import com.pacific.pacificbe.services.BookingService;
import com.pacific.pacificbe.services.WalletService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.IdUtil;
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
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.pacific.pacificbe.utils.Constant.SYS_WALLET_ID;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletServiceImpl implements WalletService {
    private final TourDetailRepository tourDetailRepository;
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final SystemWalletRepository systemWalletRepository;
    private final TransactionRepository transactionRepository;

    private final IdUtil idUtil;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;


    @Override
    @Transactional
    public BookingResponse refund(RefundRequestDTO request) {
//        Check booking
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!BookingStatus.PAID.toString().equals(booking.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        String reasonInfo = String.format(
                "[Cancellation] Reason: %s|CancelledBy: %s|DateRequested: %s",
                request.getReasons() != null ? request.getReasons().trim() : "N/A",
                "User" + " - " + booking.getUser().getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
        );

// Cap nhat trang thai booking
        booking.setStatus(BookingStatus.ON_HOLD.toString());
        booking.setNotes(reasonInfo);
        bookingRepository.save(booking);

        SystemWallet systemWallet = systemWalletRepository.findById(SYS_WALLET_ID)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        String lastTransactionId = transactionRepository.findLatestWalletTransactionIdOfToday();

        Transaction transaction = new Transaction();
        transaction.setId(idUtil.generateTransactionId(lastTransactionId));
        transaction.setWallet(systemWallet);
        transaction.setBooking(booking);
        transaction.setUser(booking.getUser());
        transaction.setAmount(booking.getTotalAmount().multiply(BigDecimal.valueOf(0.8)));
        transaction.setType(WalletStatus.REFUND_REQUEST.toString());
        transaction.setStatus(WalletStatus.PENDING.toString());
        transaction.setDescription(request.getReasons());
        transactionRepository.save(transaction);
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public void approveRefund(ApproveRefundRequestDto request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!BookingStatus.ON_HOLD.toString().equals(booking.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        String cancellationReason = booking.getNotes();
        if (cancellationReason == null || !cancellationReason.startsWith("[Cancellation]")) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION, "Missing or invalid cancellation reason");
        }

        LocalDateTime dateRequested = bookingService.extractDateRequested(cancellationReason);
        TourDetail tourDetail = booking.getTourDetail();
        Transaction paymentTransaction = transactionRepository.findByBooking_IdAndStatus(booking.getId(),
                WalletStatus.COMPLETED.toString());

        LocalDateTime paymentTime = paymentTransaction.getCreatedAt();
        long hoursSincePayment = ChronoUnit.HOURS.between(paymentTime, dateRequested);
        BigDecimal refundPercentage;
        if (hoursSincePayment <= 12) { // Hoặc <= 24 nếu muốn 1 ngày
            refundPercentage = BigDecimal.valueOf(1.0); // 100% nếu trong 12 giờ
        } else {
            // Dựa vào ngày khởi hành tour
            long daysUntilTour = ChronoUnit.DAYS.between(dateRequested, tourDetail.getStartDate());
            if (daysUntilTour >= 30) {
                refundPercentage = BigDecimal.valueOf(1.0); // 100%
            } else if (daysUntilTour >= 15) {
                refundPercentage = BigDecimal.valueOf(0.9); // 90%
            } else if (daysUntilTour >= 7) {
                refundPercentage = BigDecimal.valueOf(0.8); // 80%
            } else if (daysUntilTour >= 3) {
                refundPercentage = BigDecimal.valueOf(0.7); // 70%
            } else {
                refundPercentage = BigDecimal.valueOf(0.6); // 60%
            }
        }

        if (request.isApproved()) {
            SystemWallet systemWallet = systemWalletRepository.findById(SYS_WALLET_ID)
                    .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

            BigDecimal refundAmount = booking.getTotalAmount().multiply(refundPercentage);

            if (systemWallet.getBalance().compareTo(refundAmount) < 0) {
                throw new AppException(ErrorCode.WALLET_NOT_ENOUGH);
            }

            User user = booking.getUser();
            user.setDeposit(user.getDeposit().add(refundAmount));
            userRepository.save(user);

            // Cập nhật ví hệ thống
            systemWallet.setBalance(systemWallet.getBalance().subtract(refundAmount));
            systemWalletRepository.save(systemWallet);

            // Cập nhật giao dịch
            Transaction transaction = transactionRepository.findByBookingIdAndType(
                    booking.getId(), WalletStatus.REFUND_REQUEST.toString());
            transaction.setType(WalletStatus.REFUNDED.toString());
            transaction.setStatus(WalletStatus.COMPLETED.toString());
            transaction.setDescription(String.format(
                    "Hoàn tiền %s%% (%s) cho booking %s. Lý do: %s",
                    refundPercentage.multiply(BigDecimal.valueOf(100)),
                    refundAmount,
                    booking.getBookingNo(),
                    bookingService.extractReason(cancellationReason)));
            transactionRepository.save(transaction);

            booking.setStatus(BookingStatus.CANCELLED.toString());

            Voucher voucher = booking.getVoucher();
            if (voucher != null) {
                voucher.setQuantity(voucher.getQuantity() + 1);
                voucherRepository.save(voucher);
                booking.setVoucher(null);
            }
            if (tourDetail != null) {
                tourDetail.setQuantity(tourDetail.getQuantity() + 1);
                tourDetailRepository.save(tourDetail);
            }
        } else {
            Transaction transaction = transactionRepository.findByBookingIdAndType(booking.getId(),
                    WalletStatus.REFUND_REQUEST.toString());
            transaction.setStatus(WalletStatus.REJECTED.toString());
            transactionRepository.save(transaction);
            booking.setStatus(BookingStatus.PAID.toString());
        }

        bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public void deposit(BigDecimal amount) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setDeposit(user.getDeposit().add(amount));
        userRepository.save(user);

        String lastTransactionId = transactionRepository.findLatestWalletTransactionIdOfToday();

        Transaction transaction = new Transaction();
        transaction.setId(idUtil.generateTransactionId(lastTransactionId));
        transaction.setWallet(systemWalletRepository.findById(SYS_WALLET_ID).orElseThrow());
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setType(WalletStatus.DEPOSIT.toString());
        transaction.setStatus(WalletStatus.COMPLETED.toString());
        transaction.setDescription("Nạp tiền vào ví " + amount);
        transactionRepository.save(transaction);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getDeposit().compareTo(amount) < 0) {
            throw new AppException(ErrorCode.WALLET_NOT_ENOUGH);
        }

        user.setDeposit(user.getDeposit().subtract(amount));
        userRepository.save(user);

        String lastTransactionId = transactionRepository.findLatestWalletTransactionIdOfToday();

        Transaction transaction = new Transaction();
        transaction.setId(idUtil.generateTransactionId(lastTransactionId));
        transaction.setWallet(systemWalletRepository.findById(SYS_WALLET_ID).orElseThrow());
        transaction.setUser(user);
        transaction.setAmount(amount);
        transaction.setType(WalletStatus.WITHDRAW.toString());
        transaction.setStatus(WalletStatus.COMPLETED.toString());
        transaction.setDescription("Rút tiền " + amount);
        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionResponseDto> getTransactions(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Transaction> transactions = transactionRepository.findByUser_Id(user.getId());

        return transactions.stream().map(t -> {
            TransactionResponseDto dto = new TransactionResponseDto();
            dto.setId(t.getId());
            dto.setWalletId(t.getWallet().getId());
            dto.setUserId(t.getUser().getId());
            dto.setAmount(t.getAmount());
            dto.setType(t.getType());
            dto.setStatus(t.getStatus());
            dto.setDescription(t.getDescription());
            dto.setCreatedAt(t.getUpdatedAt());
            if (t.getBooking() != null) {
                dto.setBookingNo(t.getBooking().getBookingNo());
            } else {
                dto.setBookingNo(null); // Or set a default value if needed
            }

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
        } else if ("SYSTEM_WALLET".equals(type)) {
            SystemWallet wallet = systemWalletRepository.findById(id)
                    .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
            response.setBalance(wallet.getBalance());
        }

        return response;
    }

    @Override
    public List<RefundRequestResponseDto> getRefundRequests() {
        List<Booking> bookings = bookingRepository.findByStatus(BookingStatus.ON_HOLD.toString());

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
            List<Transaction> transactions = transactionRepository.findByBookingId(booking.getId());
            String reason = transactions.stream()
                    .filter(t -> WalletStatus.REFUND_REQUEST.toString().equals(t.getType()))
                    .findFirst()
                    .map(Transaction::getDescription)
                    .orElse("Không cung cấp lý do");

            dto.setReason(reason);
            dto.setStatus(booking.getStatus());
            dto.setCreatedAt(booking.getCreatedAt());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public SystemBalanceResponseDto getSystemBalance() {
// Lấy ví hệ thống (giả sử chỉ có một ví hệ thống với ID cố định)
        SystemWallet wallet = systemWalletRepository.findById(SYS_WALLET_ID)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        // Tính tổng tiền đã hoàn (từ các giao dịch REFUND thành công)
        BigDecimal totalRefunded = transactionRepository.sumRefundedAmount()
                .orElse(BigDecimal.ZERO);

        // Đếm tổng giao dịch
        long totalTransactions = transactionRepository.count();

        SystemBalanceResponseDto response = new SystemBalanceResponseDto();
        response.setBalance(wallet.getBalance());
        response.setTotalRefunded(totalRefunded);
        response.setTotalTransactions(totalTransactions);
        return response;
    }

    @Override
    public void depositSystemWallet(BigDecimal amount) {
        SystemWallet systemWallet = systemWalletRepository.findById(SYS_WALLET_ID)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        systemWallet.setBalance(systemWallet.getBalance().add(amount));
        systemWallet.setUpdatedAt(LocalDateTime.now());
        systemWalletRepository.save(systemWallet);
    }

    @Override
    public void withdrawSystemWallet(BigDecimal amount) {
        SystemWallet systemWallet = systemWalletRepository.findById(SYS_WALLET_ID)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        if (systemWallet.getBalance().compareTo(amount) < 0) {
            throw new AppException(ErrorCode.WALLET_NOT_ENOUGH);
        }

        systemWallet.setBalance(systemWallet.getBalance().subtract(amount));
        systemWallet.setUpdatedAt(LocalDateTime.now());
        systemWalletRepository.save(systemWallet);

    }

}
