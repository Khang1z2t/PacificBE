package com.pacific.pacificbe.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;

public enum GenderEnums {
    MALE,
    FEMALE,
    OTHER,
    CROISSANT;

    @JsonCreator
    public static GenderEnums fromValue(String value) {
        if (value == null) {
            return null; // Cho phép null, sẽ kiểm tra sau
        }
        try {
            return GenderEnums.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_GENDER);
        }
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
