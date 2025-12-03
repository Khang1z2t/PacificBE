package com.pacific.pacificbe.services.impl.oauth;

import com.pacific.pacificbe.dto.request.oauth2.GoogleTokenRequest;
import com.pacific.pacificbe.dto.request.oauth2.OAuthTokenRequest;
import com.pacific.pacificbe.dto.response.oauth2.GoogleTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.GoogleUserResponse;
import com.pacific.pacificbe.dto.response.oauth2.OAuthTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.OAuthUserResponse;
import com.pacific.pacificbe.integration.google.GoogleClient;
import com.pacific.pacificbe.integration.google.GoogleUserClient;
import com.pacific.pacificbe.services.OAuthService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.enums.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthServiceImpl implements OAuthService {
    private final GoogleClient googleClient;
    private final GoogleUserClient googleUserClient;
    private final AuthUtils authUtils;
    @Value("${oauth2.google.clientId}")
    private String googleClientId;
    @Value("${oauth2.google.clientSecret}")
    private String googleClientSecret;
    @Value("${oauth2.google.redirectUri}")
    private String googleRedirectUri;
    @Value("${oauth2.google.grantType}")
    private String googleGrantType;


    @Override
    public String getAuthorizationUrl(String redirectTo) {
//        redirectTo = authUtils.getRedirectUrl(redirectTo);
//        String state = Base64.getUrlEncoder().encodeToString(redirectTo.getBytes());
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/auth")
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", redirectTo)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email")
//                .queryParam("state", state) // Thêm state chứa redirectTo
                .build()
                .toUriString();
    }

    @Override
    public OAuthTokenResponse exchangeToken(OAuthTokenRequest request) {
        GoogleTokenResponse response = googleClient.exchangeToken(GoogleTokenRequest.builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .redirectUri(request.getRedirectUri() != null ? request.getRedirectUri() : googleRedirectUri)
                .grantType(googleGrantType)
                .code(request.getCode())
                .build());
        return OAuthTokenResponse.builder()
                .accessToken(response.getAccessToken())
                .expiresIn(response.getExpiresIn())
                .tokenType(response.getTokenType())
                .build();
    }

    @Override
    public OAuthUserResponse getUserInfo(String accessToken) {
        GoogleUserResponse response = googleUserClient.getUserInfo("Bearer " + accessToken);
        return OAuthUserResponse.builder()
                .email(response.getEmail())
                .firstName(response.getGivenName())
                .lastName(response.getFamilyName())
                .picture(response.getPicture())
                .sub(response.getSub())
                .name(response.getName())
                .build();
    }

    @Override
    public String getClientId() {
        return googleClientId;
    }

    @Override
    public String getClientSecret() {
        return googleClientSecret;
    }

    @Override
    public String getRedirectUri() {
        return googleRedirectUri;
    }

    @Override
    public String getGrantType() {
        return googleGrantType;
    }

    @Override
    public String getScope() {
        return "read write trust oauth2 google";
    }

    @Override
    public OAuthProvider getProviderType() {
        return OAuthProvider.GOOGLE;
    }
}
