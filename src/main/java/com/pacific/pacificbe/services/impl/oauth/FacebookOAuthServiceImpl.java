package com.pacific.pacificbe.services.impl.oauth;

import com.pacific.pacificbe.dto.request.oauth2.FacebookTokenRequest;
import com.pacific.pacificbe.dto.request.oauth2.OAuthTokenRequest;
import com.pacific.pacificbe.dto.response.oauth2.FacebookTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.FacebookUserResponse;
import com.pacific.pacificbe.dto.response.oauth2.OAuthTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.OAuthUserResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.integration.facebook.FacebookClient;
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
public class FacebookOAuthServiceImpl implements OAuthService {

    private final FacebookClient facebookClient;
    private final AuthUtils authUtils;
    @Value("${oauth2.facebook.clientId}")
    private String facebookClientId;
    @Value("${oauth2.facebook.clientSecret}")
    private String facebookClientSecret;
    @Value("${oauth2.facebook.redirectUri}")
    private String facebookRedirectUri;

    @Override
    public String getAuthorizationUrl(String redirectTo) {
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
    public OAuthTokenResponse exchangeToken(OAuthTokenRequest request) {
        FacebookTokenResponse response = facebookClient.exchangeToken(FacebookTokenRequest.builder()
                .clientId(facebookClientId)
                .clientSecret(facebookClientSecret)
                .redirectUri(facebookRedirectUri)
                .code(request.getCode())
                .build());
        if (response.getError() != null) {
            log.error("Error exchanging token: {}", response.getError());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION,
                    "Error exchanging token: " + response.getError());
        }
        return OAuthTokenResponse.builder()
                .accessToken(response.getAccessToken())
                .expiresIn(response.getExpiresIn())
                .tokenType(response.getTokenType())
                .build();
    }

    @Override
    public OAuthUserResponse getUserInfo(String accessToken) {
        FacebookUserResponse response = facebookClient.getUserInfo(accessToken, "id,name,email,first_name,last_name,picture");
        String pictureUrl = response.getPicture() != null
                ? response.getPicture().getData().getUrl()
                : null;
        return OAuthUserResponse.builder()
                .email(response.getEmail())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .picture(pictureUrl)
                .id(response.getId())
                .name(response.getName())
                .build();
    }

    @Override
    public String getClientId() {
        return facebookClientId;
    }

    @Override
    public String getClientSecret() {
        return facebookClientSecret;
    }

    @Override
    public String getRedirectUri() {
        return facebookRedirectUri;
    }

    @Override
    public String getGrantType() {
        return "authorization_code";
    }

    @Override
    public String getScope() {
        return "read write trust oauth2 facebook";
    }

    @Override
    public OAuthProvider getProviderType() {
        return OAuthProvider.FACEBOOK;
    }
}
