package com.pacific.pacificbe.exception;

import com.pacific.pacificbe.utils.enums.*;
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
    PASSWORD_NOT_MATCH(1014, "Password not match", HttpStatus.BAD_REQUEST),
    INVALID_OTP(1014, "Invalid OTP", HttpStatus.BAD_REQUEST),
    CANT_CHANGE_USERNAME(1015, "Can't change username", HttpStatus.BAD_REQUEST),

    USER_ROLE_INVALID(1015, "Invalid role, must be " +
            getEnumValues(UserRole.class), HttpStatus.BAD_REQUEST),
    USER_STATUS_INVALID(1016, "Invalid status, must be " +
            getEnumValues(UserStatus.class), HttpStatus.BAD_REQUEST),
    INVALID_GENDER(1017, "Invalid gender, must be " +
            getEnumValues(GenderEnums.class), HttpStatus.BAD_REQUEST),
    INVALID_AGE_GROUP(1018, "Invalid age group, must be " +
            getEnumValues(AgeGroup.class), HttpStatus.BAD_REQUEST),
    INVALID_VOUCHER(1019, "Invalid voucher", HttpStatus.BAD_REQUEST),
    INVALID_VOUCHER_DISCOUNT(1020, "Invalid voucher discount", HttpStatus.BAD_REQUEST),
    INVALID_VOUCHER_MIN_ORDER(1021, "Invalid voucher min order", HttpStatus.BAD_REQUEST),

    // Tour Error
    TOUR_NOT_FOUND(1020, "Tour not found", HttpStatus.NOT_FOUND),
    TOUR_STATUS_INVALID(1021, "Invalid status, must be " +
            getEnumValues(TourStatus.class), HttpStatus.BAD_REQUEST),


    TOUR_DETAIL_NOT_FOUND(1030, "Tour detail not found", HttpStatus.NOT_FOUND),
    TOUR_DETAIL_STATUS_INVALID(1031, "Invalid status, must be " +
            getEnumValues(TourDetailStatus.class), HttpStatus.BAD_REQUEST),

    CATEGORY_NOT_FOUND(1040, "Category not found", HttpStatus.NOT_FOUND),
    GUIDE_NOT_FOUND(1050, "Guide not found", HttpStatus.NOT_FOUND),
    DESTINATION_NOT_FOUND(1060, "Destination not found", HttpStatus.NOT_FOUND),
    COMBO_NOT_FOUND(1070, "Combo not found", HttpStatus.NOT_FOUND),
    HOTEL_NOT_FOUND(1071, "Hotel not found", HttpStatus.NOT_FOUND),
    TRANSPORT_NOT_FOUND(1072, "Transport not found", HttpStatus.NOT_FOUND),
    USER_NOT_AUTHENTICATED(1098, "You need login first", HttpStatus.UNAUTHORIZED),
    CANT_SEND_MAIL(1099, "Can't send mail", HttpStatus.INTERNAL_SERVER_ERROR),

    // Image and File related errors
    INVALID_IMAGE(1100, "Invalid image file", HttpStatus.BAD_REQUEST),
    UPLOAD_IMAGE_FAILED(1101, "Failed to upload image", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_NOT_FOUND(1102, "Image not found", HttpStatus.NOT_FOUND),
    IMAGE_SIZE_EXCEEDED(1103, "Image size exceeds maximum limit", HttpStatus.BAD_REQUEST),
    UNSUPPORTED_IMAGE_FORMAT(1104, "Unsupported image format", HttpStatus.BAD_REQUEST),

    // Transport specific errors
    TRANSPORT_IN_USE(1200, "Transport is in use by one or more tours", HttpStatus.BAD_REQUEST),
    INVALID_TRANSPORT_TYPE(1201, "Invalid transport type", HttpStatus.BAD_REQUEST),
    TRANSPORT_ALREADY_EXISTS(1202, "Transport with this name already exists", HttpStatus.BAD_REQUEST),
    //REPORTS
    REVENUE_NOT_FOUND(1080, "Revenue not found", HttpStatus.NOT_FOUND),

    // Thêm hằng số CATEGORY_IN_USE
    CATEGORY_IN_USE(1041, "Category is in use by one or more tours", HttpStatus.BAD_REQUEST),

    // OrderInfo
    ORDERINFO_NOT_FOUND(1042, "OrderInfo not found", HttpStatus.NOT_FOUND),
    //    ORDER INFO OR BOOKING
    ORDER_INFO_NOT_FOUND(1042, "OrderInfo not found", HttpStatus.NOT_FOUND),
    INVALID_ORDER_INFO(1043, "Invalid orderinfo or smthing", HttpStatus.BAD_REQUEST),

    BOOKING_NOT_FOUND(1044, "Booking not found", HttpStatus.NOT_FOUND),
    // Admin User Errors
    INVALID_STATUS(1021, "Invalid status, must be ACTIVE or INACTIVE", HttpStatus.BAD_REQUEST),

    // Blog Errors
    TITLE_NOT_FOUND(1022, "Title not found", HttpStatus.NOT_FOUND),
    BLOG_NOT_FOUND(1023, "Blog not found", HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND(1024, "File image not found", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED(1025, "Upload image failed", HttpStatus.BAD_REQUEST),

    // Review Errors
    RATING_NOT_FOUND(1026, "Rating not found", HttpStatus.NOT_FOUND),
    INVALID_RATING_STATUS(1027, "Invalid status, must be ACTIVE or INACTIVE", HttpStatus.NOT_FOUND),
    REVIEW_NOT_FOUND(1034, "Review not found", HttpStatus.NOT_FOUND),


    // Voucher Errors
    VOUCHER_NOT_FOUND(1028, "Voucher not found", HttpStatus.NOT_FOUND),
    INVALID_VOUCHER_STATUS(1029, "Invalid status, must be " +
            getEnumValues(VoucherStatus.class), HttpStatus.NOT_FOUND),
    INVALID_DATE_RANGE(1030, "Invalid date range", HttpStatus.NOT_FOUND),
    INVALID_ID(1031, "Invalid ID", HttpStatus.NOT_FOUND),
    INVALID_APPLY_TO(1032, "Invalid type of apply to, must be " +
            getEnumValues(ApplyTo.class), HttpStatus.NOT_FOUND),
    VOUCHER_CODE_NOT_FOUND(1033, "Voucher code not found", HttpStatus.NOT_FOUND),
    VOUCHER_TOUR_ID_REQUIRED(1034, "Tour ID is required", HttpStatus.BAD_REQUEST),
    VOUCHER_CATEGORY_ID_REQUIRED(1035, "Category ID is required", HttpStatus.BAD_REQUEST),
    INVALID_MIN_ORDER_VALUE(1036, "Min order value must be greater than 0", HttpStatus.BAD_REQUEST),
    VOUCHER_MAX_DISCOUNT_INVALID(1037, "Max discount must be greater than 0", HttpStatus.BAD_REQUEST),
//    Itinerary
    ITINERARY_NOT_FOUND(1038, "Itinerary not found", HttpStatus.NOT_FOUND),


    // Support Errors
    SUPPORT_NOT_FOUND(1040, "Support not found", HttpStatus.NOT_FOUND),
    INVALID_SUPPORT_STATUS(1041, "Invalid status, must be ACTIVE or INACTIVE", HttpStatus.NOT_FOUND),
    INVALID_INPUT(1042, "Check error", HttpStatus.NOT_FOUND),

    //  util
    CANNOT_GENERATE_ICS(2000, "Cannot generate ICS file", HttpStatus.INTERNAL_SERVER_ERROR),
    CANNOT_GENERATE_QR(2001, "Cannot generate QR code", HttpStatus.INTERNAL_SERVER_ERROR),

    //Vi (Wallet)
    WALLET_NOT_FOUND(2002, "Wallet not found", HttpStatus.NOT_FOUND),
    WALLET_NOT_ENOUGH(2003, "Wallet not enough", HttpStatus.NOT_FOUND),
    INVALID_REFUND_AMOUNT(2004, "Invalid refund amount", HttpStatus.NOT_FOUND),
    WALLET_TRANSACTION_NOT_FOUND(2003, "Wallet transaction not found", HttpStatus.NOT_FOUND);

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
