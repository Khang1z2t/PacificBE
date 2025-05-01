package com.pacific.pacificbe.utils.enums;

import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;

public enum OAuthProvider {
    GOOGLE,
    FACEBOOK,
    DISCORD,
    ;

    public static OAuthProvider fromString(String type) {
        return switch (type.toLowerCase()) {
            case "google" -> GOOGLE;
            case "facebook" -> FACEBOOK;
            case "discord" -> DISCORD;
            default -> throw new AppException(
                    ErrorCode.UNCATEGORIZED_EXCEPTION, "Invalid OAuth provider type: " + type);
        };
    }
}
