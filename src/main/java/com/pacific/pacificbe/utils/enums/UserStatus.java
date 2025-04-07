package com.pacific.pacificbe.utils.enums;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE,
    INACTIVE,
    BLOCKED,
    REQUIRE_USERNAME_PASSWORD_CHANGE,
    REQUIRE_PASSWORD_CHANGE,
    REQUIRE_USERNAME_CHANGE,
}
