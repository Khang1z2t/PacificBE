package com.pacific.pacificbe.exception;

import lombok.Getter;
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

    NEED_LOGIN(1098, "You need login first", HttpStatus.UNAUTHORIZED),
    CANT_SEND_MAIL(1099, "Can't send mail", HttpStatus.INTERNAL_SERVER_ERROR),


//    Tour Error
    TOUR_NOT_FOUND(2001, "Tour not found", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final HttpStatusCode statusCode;
    private final String message;
}
