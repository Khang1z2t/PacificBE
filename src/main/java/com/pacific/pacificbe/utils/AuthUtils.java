package com.pacific.pacificbe.utils;

import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.model.SystemWallet;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.SystemWalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import static com.pacific.pacificbe.utils.Constant.SYS_WALLET_ID;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final SystemWalletRepository systemWalletRepository;

    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            User userDetails = (User) authentication.getPrincipal();
            return userDetails.getId(); // Giả định rằng bạn đã tùy chỉnh UserDetails để chứa ID của người dùng
        }
        throw new AppException(ErrorCode.USER_NOT_AUTHENTICATED);
    }

    public SystemWallet getSystemWallet() {
        return systemWalletRepository.findById(SYS_WALLET_ID).orElseThrow(
                () -> new AppException(ErrorCode.WALLET_NOT_FOUND));
    }
}
