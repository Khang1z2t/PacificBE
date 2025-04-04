package com.pacific.pacificbe.utils.enums;

public enum BookingStatus {
    PENDING, // chờ thanh toán
    PAID, // đã thanh toán
    EXPIRED, // hết hạn nếu ko thanh toán
    ON_GOING, // đang diễn ra
    COMPLETED, // đã hoàn thành
    ON_HOLD, // đang giữ (đợi thanh toán hoặc đợi xác nhận hoàn hoặc nguyên nhân thứ 3)
    CANCELLED, // đã huỷ (người dùng huỷ hoặc admin huỷ)


    CONFIRMED, // đã xác nhận (nếu có duyệt)
}
