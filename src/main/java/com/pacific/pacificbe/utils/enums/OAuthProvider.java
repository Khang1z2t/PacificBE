package com.pacific.pacificbe.utils.enums;

public enum OAuthProvider {
    GOOGLE,
    FACEBOOK;

    public static OAuthProvider fromString(String type) {
        return switch (type.toLowerCase()) {
            case "google" -> GOOGLE;
            case "facebook" -> FACEBOOK;
            default -> throw new IllegalArgumentException("Unknown OAuth provider: " + type);
        };
    }
}
