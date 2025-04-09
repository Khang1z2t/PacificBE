package com.pacific.pacificbe.utils.enums;

public enum GuideStatus {
    ACTIVE,      // Có thể tham gia tour (rảnh rỗi, sẵn sàng)
    INACTIVE,    // Đang tham gia tour khác (bận)
    SUSPENDED,   // Tạm ngưng hoạt động (do vi phạm, nghỉ dài hạn, v.v.)
    ON_LEAVE,    // Nghỉ phép (ngắn hạn hoặc dài hạn)
    RETIRED,     // Nghỉ hưu/nghỉ việc (không còn làm việc)
}
