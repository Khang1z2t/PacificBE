package com.pacific.pacificbe.services.impl.oauth;

import com.pacific.pacificbe.dto.request.oauth2.DiscordTokenRequest;
import com.pacific.pacificbe.dto.request.oauth2.OAuthTokenRequest;
import com.pacific.pacificbe.dto.response.oauth2.DiscordTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.DiscordUserResponse;
import com.pacific.pacificbe.dto.response.oauth2.OAuthTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.OAuthUserResponse;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import com.pacific.pacificbe.integration.discord.DiscordClient;
import com.pacific.pacificbe.services.OAuthService;
import com.pacific.pacificbe.utils.AuthUtils;
import com.pacific.pacificbe.utils.enums.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscordOauthServiceImpl implements OAuthService {
    private final AuthUtils authUtils;
    private final DiscordClient discordClient;
    @Value("${oauth2.discord.clientId}")
    private String discordClientId;
    @Value("${oauth2.discord.clientSecret}")
    private String discordClientSecret;
    @Value("${oauth2.discord.redirectUri}")
    private String discordRedirectUri;

    @Override
    public String getAuthorizationUrl(String redirectTo) {
//        redirectTo = authUtils.getRedirectUrl(redirectTo);
//        String state = Base64.getUrlEncoder().encodeToString(redirectTo.getBytes());
        return UriComponentsBuilder.fromUriString("https://discord.com/api/oauth2/authorize")
                .queryParam("client_id", discordClientId)
                .queryParam("redirect_uri", redirectTo)
                .queryParam("response_type", "code")
                .queryParam("scope", "identify email")
//                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public OAuthTokenResponse exchangeToken(OAuthTokenRequest request) {
        String auth = discordClientId + ":" + discordClientSecret;
        String authorization = "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", request.getCode());
        formData.add("redirect_uri", request.getRedirectUri() != null ? request.getRedirectUri() : discordRedirectUri);

        DiscordTokenResponse response = discordClient.exchangeToken(
                authorization,
                formData);

        if (response.getError() != null) {
            log.error("Error exchanging token: {}", response.getError());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION,
                    "Error exchanging discord token: " + response.getError());
        }
        return OAuthTokenResponse.builder()
                .accessToken(response.getAccessToken())
                .tokenType(response.getTokenType())
                .expiresIn((long) response.getExpiresIn())
                .build();
    }

    @Override
    public OAuthUserResponse getUserInfo(String accessToken) {
        DiscordUserResponse response = discordClient.getUserInfo("Bearer " + accessToken);
        if (response.getError() != null) {
            log.error("Error getting user info: {}", response.getError());
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION,
                    "Error getting discord user info: " + response.getError());
        }
        String firstName = null;
        String lastName = null;
        String globalName = response.getGlobalName();

        if (globalName != null && !globalName.trim().isEmpty()) {
            globalName = globalName.replaceAll("\\s+", " ").trim();
            String[] words = globalName.split(" ");

            if (words.length == 1) {
                firstName = globalName;
            } else {
                // Lấy từ cuối làm lastName, phần còn lại là firstName
                lastName = words[words.length - 1];
                StringBuilder firstNameBuilder = new StringBuilder();
                for (int i = 0; i < words.length - 1; i++) {
                    firstNameBuilder.append(words[i]).append(" ");
                }
                firstName = firstNameBuilder.toString().trim();

                if (lastName.length() < 2) {
                    firstName = globalName;
                    lastName = null;
                }
            }
        }

        return OAuthUserResponse.builder()
                .email(response.getEmail())
                .firstName(firstName)
                .lastName(lastName)
                .picture(response.getAvatar())
                .name(response.getUsername())
                .id(response.getId())
                .build();
    }

    @Override
    public String getClientId() {
        return "";
    }

    @Override
    public String getClientSecret() {
        return "";
    }

    @Override
    public String getRedirectUri() {
        return "";
    }

    @Override
    public String getGrantType() {
        return "";
    }

    @Override
    public String getScope() {
        return "read write trust oauth2 discord";
    }

    @Override
    public OAuthProvider getProviderType() {
        return OAuthProvider.DISCORD;
    }
}
