package com.pacific.pacificbe.services;

import com.pacific.pacificbe.dto.request.oauth2.OAuthTokenRequest;
import com.pacific.pacificbe.dto.response.oauth2.OAuthTokenResponse;
import com.pacific.pacificbe.dto.response.oauth2.OAuthUserResponse;
import com.pacific.pacificbe.utils.enums.OAuthProvider;

public interface OAuthService {
    OAuthTokenResponse exchangeToken(OAuthTokenRequest request);
    OAuthUserResponse getUserInfo(String accessToken);
    String getClientId();
    String getClientSecret();
    String getRedirectUri();
    String getGrantType();
    String getScope();
    OAuthProvider getProviderType();
}
