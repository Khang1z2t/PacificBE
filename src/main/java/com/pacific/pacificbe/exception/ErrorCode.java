package com.pacific.pacificbe.exception;

import com.pacific.pacificbe.utils.enums.TourDetailStatus;
import com.pacific.pacificbe.utils.enums.TourStatus;
import com.pacific.pacificbe.utils.enums.UserRole;
import com.pacific.pacificbe.utils.enums.UserStatus;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(1008, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1009, "Username already exists", HttpStatus.BAD_REQUEST),
    EMAIL_ALREADY_EXISTS(1010, "Email already exists", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1011, "Invalid token", HttpStatus.BAD_REQUEST),
    USER_NOT_ACTIVE(1012, "User is not active", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME_OR_PASSWORD(1013, "Invalid username or password", HttpStatus.BAD_REQUEST),
    INVALID_OTP(1014, "Invalid OTP", HttpStatus.BAD_REQUEST),

    USER_ROLE_INVALID(1015, "Invalid role, must be " + getEnumValues(UserRole.class), HttpStatus.BAD_REQUEST),
    USER_STATUS_INVALID(1016, "Invalid status, must be " + getEnumValues(UserStatus.class), HttpStatus.BAD_REQUEST),

    // Tour Error
    TOUR_NOT_FOUND(1020, "Tour not found", HttpStatus.NOT_FOUND),
    TOUR_STATUS_INVALID(1021, "Invalid status, must be " + getEnumValues(TourStatus.class), HttpStatus.BAD_REQUEST),


    TOUR_DETAIL_NOT_FOUND(1030, "Tour detail not found", HttpStatus.NOT_FOUND),
    TOUR_DETAIL_STATUS_INVALID(1031, "Invalid status, must be " + getEnumValues(TourDetailStatus.class), HttpStatus.BAD_REQUEST),

    CATEGORY_NOT_FOUND(1040, "Category not found", HttpStatus.NOT_FOUND),
    GUIDE_NOT_FOUND(1050, "Guide not found", HttpStatus.NOT_FOUND),
    DESTINATION_NOT_FOUND(1060, "Destination not found", HttpStatus.NOT_FOUND),
    COMBO_NOT_FOUND(1070, "Combo not found", HttpStatus.NOT_FOUND),
    HOTEL_NOT_FOUND(1071, "Hotel not found", HttpStatus.NOT_FOUND),
    TRANSPORT_NOT_FOUND(1072, "Transport not found", HttpStatus.NOT_FOUND),
    NEED_LOGIN(1098, "You need login first", HttpStatus.UNAUTHORIZED),
    CANT_SEND_MAIL(1099, "Can't send mail", HttpStatus.INTERNAL_SERVER_ERROR),


    // Thêm hằng số CATEGORY_IN_USE
    CATEGORY_IN_USE(1041, "Category is in use by one or more tours", HttpStatus.BAD_REQUEST),

    // Admin User Errors
    INVALID_STATUS(1021, "Invalid status, must be ACTIVE or INACTIVE", HttpStatus.BAD_REQUEST),

    // Blog Errors
    TITLE_NOT_FOUND(1022, "Title not found",HttpStatus.NOT_FOUND),
    BLOG_NOT_FOUND(1023, "Blog not found",HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND(1024, "File image not found",HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED(1025, "Upload image failed", HttpStatus.BAD_REQUEST);


    //  ^---------------------
    //  Lưu ý ở đây phải kết thúc enum bằng dấu ; chứ không phải dấu ,

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;

    private static <E extends Enum<E>> String getEnumValues(Class<E> enumClass) {
        return String.join(" or ", EnumUtils.getEnumMap(enumClass).keySet());
    }
}
