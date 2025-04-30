package com.pacific.pacificbe.utils;

import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.model.SystemWallet;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.SystemWalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pacific.pacificbe.utils.Constant.SYS_WALLET_ID;

@Slf4j
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

    public String getRedirectUrl(String redirectUrl) {
        String defaultUrl = allowedRedirectUrls.getFirst();

        if (redirectUrl == null) {
            log.warn("Redirect URL is null: returning default URL {}", defaultUrl);
            return defaultUrl;
        }

        String domain = getDomainFromUrl(redirectUrl);
        if (domain == null) {
            log.warn("Invalid redirect URL: {}. Returning default URL {}", redirectUrl, defaultUrl);
            return defaultUrl;
        }

        String matchedUrl = domainToUrl.get(domain);
        if (matchedUrl == null) {
            log.warn("Invalid domain: {}. Returning default URL {}", domain, defaultUrl);
            return defaultUrl;
        }

        return matchedUrl;
    }

    private final List<String> allowedRedirectUrls = Arrays.asList(
            Constant.FE_LOCAL_URL,
            Constant.FE_PROD_URL,
            Constant.FE_PROD_URL_2
    );

    private final Map<String, String> domainToUrl = allowedRedirectUrls.stream()
            .collect(Collectors.toMap(
                    this::getDomainFromUrl,
                    url -> url,
                    (u1, u2) -> u1 // Keep the first URL if there are duplicates
            ));

    private String getDomainFromUrl(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host == null) {
                return null;
            }
            return host.startsWith("www.") ? host.substring(4) : host;
        } catch (URISyntaxException e) {
            log.warn("Invalid URL format: {}", url);
            return null;
        }
    }
}
