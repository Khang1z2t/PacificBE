package com.pacific.pacificbe.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AppException extends RuntimeException {
    public AppException(ErrorCode errorCode, String message) {
        super(message != null && !message.isEmpty() ? message : errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    private ErrorCode errorCode;

}
