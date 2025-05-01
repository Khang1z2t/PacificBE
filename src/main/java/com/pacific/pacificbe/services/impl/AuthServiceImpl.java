package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.dto.request.*;
import com.pacific.pacificbe.dto.request.oauth2.FacebookTokenRequest;
import com.pacific.pacificbe.dto.request.oauth2.GoogleTokenRequest;
import com.pacific.pacificbe.dto.request.oauth2.OAuthTokenRequest;
import com.pacific.pacificbe.dto.response.AuthenticationResponse;
import com.pacific.pacificbe.dto.response.UserRegisterResponse;
import com.pacific.pacificbe.dto.response.UserResponse;
import com.pacific.pacificbe.dto.response.oauth2.*;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.integration.facebook.FacebookClient;
import com.pacific.pacificbe.mapper.UserMapper;
import com.pacific.pacificbe.model.User;
import com.pacific.pacificbe.repository.UserRepository;
import com.pacific.pacificbe.integration.google.GoogleClient;
import com.pacific.pacificbe.integration.google.GoogleUserClient;
import com.pacific.pacificbe.services.*;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.Constant;
import com.pacific.pacificbe.utils.IdUtil;
import com.pacific.pacificbe.utils.UrlMapping;
import com.pacific.pacificbe.utils.enums.OAuthProvider;
import com.pacific.pacificbe.utils.enums.UserRole;
import com.pacific.pacificbe.utils.enums.UserStatus;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.pacific.pacificbe.utils.Constant.DEFAULT_AVATAR;
import static com.pacific.pacificbe.utils.UrlMapping.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final GoogleClient googleClient;
    private final GoogleUserClient googleUserClient;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final IdUtil idUtil;
    private final OtpService otpService;
    private final MailService mailService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final AuthUtils authUtils;
    private final FacebookClient facebookClient;
    private final Collection<OAuthService> oAuthServices;
    @Value("${oauth2.google.clientId}")
    private String googleClientId;
    @Value("${oauth2.google.clientSecret}")
    private String googleClientSecret;
    @Value("${oauth2.google.redirectUri}")
    private String googleRedirectUri;
    @Value("${oauth2.google.grantType}")
    private String googleGrantType;

    @Value("${oauth2.facebook.clientId}")
    private String facebookClientId;
    @Value("${oauth2.facebook.clientSecret}")
    private String facebookClientSecret;
    @Value("${oauth2.facebook.redirectUri}")
    private String facebookRedirectUri;

    @Override
    public AuthenticationResponse loginUser(LoginRequest request) {
        // Mọi logic đều xử lý ở service
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getIdentifier(),
                            request.getPassword())
            );

            User user = (User) authentication.getPrincipal();

            if (!user.isActive()) throw new AppException(ErrorCode.USER_NOT_ACTIVE);

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("scope", "read write trust");
            var jwtToken = jwtService.generateToken(extraClaims, user);

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .username(user.getUsername())
                    .role(user.getRole())
                    .build();
        } catch (AuthenticationException e) {
            throw new AppException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }
    }

    @Override
    public UserRegisterResponse registerUser(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS, "Tên người dùng đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS, "Email đã tồn tại");
        }
        if (userRepository.existsByUsernameAndEmail(request.getUsername(), request.getEmail())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS, "Tài khoản đã tồn tại");
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setRole(UserRole.USER.toString());
        user.setAvatarUrl(DEFAULT_AVATAR);
        user.setStatus(UserStatus.ACTIVE.toString());
        user = userRepository.save(user);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("scope", "read write trust");

        return UserRegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .accessToken(jwtService
                        .generateToken(extraClaims, user))
                .build();
    }

    @Override
    public UserResponse authenticateToken() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            if (!user.isActive()) throw new AppException(ErrorCode.USER_NOT_ACTIVE);
            if (user.getUsername().equals(user.getEmail().split("@")[0])) {
                if (!user.getStatus().equals(UserStatus.REQUIRE_USERNAME_PASSWORD_CHANGE.toString())) {
                    user.setStatus(UserStatus.REQUIRE_USERNAME_CHANGE.toString());
                }
            } else if (user.getStatus().equals(UserStatus.REQUIRE_USERNAME_PASSWORD_CHANGE.toString())) {
                user.setStatus(UserStatus.REQUIRE_PASSWORD_CHANGE.toString());
            } else {
                user.setStatus(UserStatus.ACTIVE.toString());
            }
            userRepository.save(user);
            return userMapper.toUserResponse(user);
        } catch (ExpiredJwtException e) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        } catch (Exception e) {
            // Handle other authentication errors
            log.error("Error during authentication: {}", e.getMessage(), e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public String sendEmailVerify(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_AUTHENTICATED));
        String otp = otpService.generateOtp(email);
        sendMailVerify(user, otp);
        return "Gửi email thành công";
    }

    @Override
    public String sendEmailResetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String otp = otpService.generateOtp(email);
        sendMailForgotPassword(user, otp);
        return "Gửi email thành công";
    }


    @Override
    public boolean verifyEmail(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (otpService.verifyOtp(request.getEmail(), request.getOtp())) {
            user.setEmailVerified(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }


    @Override
    public boolean verifyResetPassword(VerifyOtpRequest request) {
        return otpService.verifyOtp(request.getEmail(), request.getOtp());
    }

    @Override
    public boolean resetPassword(ResetUserPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (request.getNewPassword().equals(request.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setStatus(UserStatus.ACTIVE.toString());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public String getGoogleUrl(String redirectTo) {
        redirectTo = authUtils.getRedirectUrl(redirectTo);
        String state = Base64.getUrlEncoder().encodeToString(redirectTo.getBytes());
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email")
                .queryParam("state", state) // Thêm state chứa redirectTo
                .build()
                .toUriString();
    }

    @Override
    public RedirectView loginGoogleCallback(String code, String error, String state) {
        log.debug("Google login callback: code={}, error={}, state={}", code, error, state);
        String redirectTo = authUtils.getRedirectUrl(state);
        String redirectBaseUrl = redirectTo + GOOGLE_REDIRECT;
        log.debug("Redirecting to: {}", redirectBaseUrl);
        if (error != null) {
            log.error("Error from google: {}", error);
            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("error", error)
                    .build()
                    .toUriString();
            return new RedirectView(url);
        }
        try {
            log.debug("Exchanging token with Google API...");
            GoogleTokenResponse response = googleClient.exchangeToken(GoogleTokenRequest.builder()
                    .clientId(googleClientId)
                    .clientSecret(googleClientSecret)
                    .redirectUri(googleRedirectUri)
                    .grantType(googleGrantType)
                    .code(code)
                    .build());
            log.info("Successfully exchanged token. Access token: {}", response.getAccessToken());

            log.debug("Fetching user info from Google...");
            GoogleUserResponse userInfo = googleUserClient.getUserInfo("Bearer " + response.getAccessToken());
            log.info("User info retrieved: email={}", userInfo.getEmail());
            User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
                    () -> userRepository.save(User.builder()
                            .email(userInfo.getEmail())
                            .username(userInfo.getEmail().split("@")[0] + "-google")
                            .firstName(userInfo.getGivenName())
                            .lastName(userInfo.getFamilyName())
                            .avatarUrl(idUtil.getIdAvatar(userInfo.getPicture()))
                            .status(UserStatus.REQUIRE_USERNAME_PASSWORD_CHANGE.toString())
                            .password(passwordEncoder.encode(idUtil.generateRandomPassword()))
                            .role(UserRole.USER.toString())
                            .emailVerified(true)
                            .active(true)
                            .build()));

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("scope", "read write trust oauth2 google");

            String accessToken = jwtService.generateToken(extraClaims, user);

            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("refresh_token", accessToken)
                    .build()
                    .toUriString();
            return new RedirectView(url);
        } catch (Exception e) {
            log.error("Error during Google login callback: {}", e.getMessage(), e);
            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("error", "server_error")
                    .build()
                    .toUriString();
            return new RedirectView(url);
        }
    }

    @Override
    public String getFacebookUrl(String redirectTo) {
        redirectTo = authUtils.getRedirectUrl(redirectTo);
        String state = Base64.getUrlEncoder().encodeToString(redirectTo.getBytes());
        return UriComponentsBuilder.fromUriString("https://www.facebook.com/v21.0/dialog/oauth")
                .queryParam("client_id", facebookClientId)
                .queryParam("redirect_uri", facebookRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "public_profile,email")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public RedirectView loginFacebookCallback(String code, String error, String state) {
        log.debug("Facebook login callback: code={}, error={}, state={}", code, error, state);
        String redirectTo = authUtils.getRedirectUrl(state);
        String redirectBaseUrl = redirectTo + FACEBOOK_REDIRECT;
        log.debug("Redirecting to: {}", redirectBaseUrl);
        if (error != null) {
            log.error("Error from facebook: {}", error);
            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("error", error)
                    .build()
                    .toUriString();
            return new RedirectView(url);
        }
        try {
            FacebookTokenResponse response = facebookClient.exchangeToken(FacebookTokenRequest.builder()
                    .clientId(facebookClientId)
                    .clientSecret(facebookClientSecret)
                    .redirectUri(facebookRedirectUri)
                    .code(code)
                    .build());
            log.info("Successfully exchanged token. Access token: {}", response.getAccessToken());
            log.debug("Fetching user info from Facebook...");
            FacebookUserResponse userInfo = facebookClient.getUserInfo(response.getAccessToken(),
                    "id,name,email,first_name,last_name,picture");
            log.info("User info retrieved: email={}", userInfo.getEmail());
            User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
                    () -> userRepository.save(User.builder()
                            .email(userInfo.getEmail())
                            .username(userInfo.getEmail().split("@")[0] + "-facebook")
                            .firstName(userInfo.getFirstName())
                            .lastName(userInfo.getLastName())
                            .avatarUrl(DEFAULT_AVATAR)
                            .status(UserStatus.REQUIRE_USERNAME_PASSWORD_CHANGE.toString())
                            .password(passwordEncoder.encode(idUtil.generateRandomPassword()))
                            .role(UserRole.USER.toString())
                            .emailVerified(true)
                            .active(true)
                            .build()));

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("scope", "read write trust oauth2 facebook");

            String accessToken = jwtService.generateToken(extraClaims, user);

            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("refresh_token", accessToken)
                    .build()
                    .toUriString();
            return new RedirectView(url);

        } catch (Exception e) {
            log.error("Error during Facebook login callback: {}", e.getMessage(), e);
            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("error", "server_error")
                    .build()
                    .toUriString();
            return new RedirectView(url);
        }
    }

    @Override
    public RedirectView loginOAuthCallback(String type, String code, String error, String state) {
        log.debug("OAuth login callback: type={}, code={}, error={}, state={}", type, code, error, state);
        OAuthProvider provider = OAuthProvider.fromString(type);
        OAuthService oauthService = oAuthServices.stream()
                .filter(service -> service.getProviderType() == provider)
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND));

        String redirectTo = authUtils.getRedirectUrl(state);
        String redirectBaseUrl = redirectTo + OAUTH2_REDIRECT;
        log.debug("Redirecting to: {}", redirectBaseUrl);
        if (error != null) {
            log.error("Error from {}: {}", type, error);
            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("error", error)
                    .build()
                    .toUriString();
            return new RedirectView(url);
        }
        try {
            log.debug("Exchanging token with {} API...", type);
            OAuthTokenResponse response = oauthService.exchangeToken(OAuthTokenRequest.builder()
                    .clientId(oauthService.getClientId())
                    .clientSecret(oauthService.getClientSecret())
                    .redirectUri(oauthService.getRedirectUri())
                    .grantType(oauthService.getGrantType())
                    .code(code)
                    .build());
            log.info("Successfully exchanged token. Access token: {}", response.getAccessToken());

            log.debug("Fetching user info from {}...", type);
            OAuthUserResponse userInfo = oauthService.getUserInfo(response.getAccessToken());
            log.info("User info retrieved: email={}", userInfo.getEmail());

            User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(
                    () -> userRepository.save(User.builder()
                            .email(userInfo.getEmail())
                            .username(userInfo.getEmail().split("@")[0] + "-" + type.toLowerCase())
                            .firstName(userInfo.getFirstName())
                            .lastName(userInfo.getLastName())
                            .avatarUrl(idUtil.getIdAvatar(userInfo.getPicture()))
                            .status(UserStatus.REQUIRE_USERNAME_PASSWORD_CHANGE.toString())
                            .password(passwordEncoder.encode(idUtil.generateRandomPassword()))
                            .role(UserRole.USER.toString())
                            .emailVerified(true)
                            .active(true)
                            .build()));

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("scope", oauthService.getScope());

            String accessToken = jwtService.generateToken(extraClaims, user);

            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("refresh_token", accessToken)
                    .build()
                    .toUriString();
            return new RedirectView(url);

        } catch (Exception e) {
            log.error("Error during {} login callback: {}", type, e.getMessage(), e);
            String url = UriComponentsBuilder.fromUriString(redirectBaseUrl)
                    .queryParam("error", "server_error")
                    .build()
                    .toUriString();
            return new RedirectView(url);
        }
    }

    @Override
    public boolean changePassword(ChangePasswordRequest request) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            if (request.getNewPassword().equals(request.getConfirmPassword())) {
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                user.setStatus(UserStatus.ACTIVE.toString());
                userRepository.save(user);
                return true;
            } else {
                throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
            }
        }
        return false;
    }

    @Override
    public boolean updateUsername(String username) {
        String userId = AuthUtils.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (userRepository.existsByUsername(username)) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        LocalDateTime now = LocalDateTime.now();
        if (user.getUsernameLastChanged() != null) {
            long daysSinceLastChange = ChronoUnit.DAYS.between(user.getUsernameLastChanged(), now);
            if (daysSinceLastChange < 7) {
                return false;
            }
        }

        user.setUsername(username);
        user.setStatus(UserStatus.ACTIVE.toString());
        user.setUsernameLastChanged(now);
        user.setUsernameChangeCount(user.getUsernameChangeCount() + 1);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUsernameAndPassword(UpdateUsernameAndPassRequest request) {
        return false;
    }


    private void sendMailVerify(User user, String otp) {
        String subjectEmail = otp + " là mã xác nhận email của bạn";
        String bodyEmail = "<h2>Xác nhận email</h2>"
                + "<p>Xin chào " + user.getFirstName() + " " + user.getLastName() + ",</p>"
                + "<p>Vui lòng sử dụng mã xác nhận bên dưới để hoàn tất việc xác nhận email:</p>"
                + "<h3 style='color:blue;'>" + otp + "</h3>"
                + "<p>Otp sẽ hết hạn trong vòng 30 phút.</p>"
                + "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>";
        mailService.queueEmail(user.getEmail(), subjectEmail, bodyEmail);
    }

    private void sendMailForgotPassword(User user, String otp) {
        String subjectEmail = otp + " là mã xác nhận đổi mật khẩu của bạn";
        String bodyEmail = "<h2>Xác nhận đổi mật khẩu</h2>"
                + "<p>Xin chào " + user.getFirstName() + " " + user.getLastName() + ",</p>"
                + "<p>Vui lòng sử dụng mã xác nhận bên dưới để hoàn tất việc đổi mật khẩu:</p>"
                + "<h3 style='color:blue;'>" + otp + "</h3>"
                + "<p>Otp sẽ hết hạn trong vòng 30 phút.</p>"
                + "<p>Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.</p>";
        mailService.queueEmail(user.getEmail(), subjectEmail, bodyEmail);
    }
}
