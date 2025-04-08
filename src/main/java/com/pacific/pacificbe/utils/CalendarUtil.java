package com.pacific.pacificbe.utils;

import com.pacific.pacificbe.exception.AppException;
import com.pacific.pacificbe.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalendarUtil {
    public String generateGoogleCalendarLink(String bookingNo, LocalDateTime startDate, LocalDateTime endDate, String description) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
        String startDateStr = startDate.atZone(java.time.ZoneOffset.UTC).format(formatter);
        String endDateStr = endDate.atZone(java.time.ZoneOffset.UTC).format(formatter);

        return "https://www.google.com/calendar/render?action=TEMPLATE"
                + "&text=" + URLEncoder.encode("Booking #" + bookingNo, StandardCharsets.UTF_8)
                + "&dates=" + startDateStr + "/" + endDateStr
                + "&details=" + URLEncoder.encode(description, StandardCharsets.UTF_8)
//                + "&location=" + URLEncoder.encode(location, StandardCharsets.UTF_8)
                + "&sf=true&output=xml";
    }

    public byte[] generateICSFile(String bookingNo, LocalDateTime startDate, LocalDateTime endDate, String description) {
        try {
            DateTimeFormatter icsFormat = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
            String startDateStr = startDate.atZone(java.time.ZoneOffset.UTC).format(icsFormat);
            String endDateStr = endDate.atZone(java.time.ZoneOffset.UTC).format(icsFormat);

            String icsContent = "BEGIN:VCALENDAR\n" +
                    "VERSION:2.0\n" +
                    "BEGIN:VEVENT\n" +
                    "SUMMARY:Booking #" + bookingNo + "\n" +
                    "DTSTART:" + startDateStr + "\n" +
                    "DTEND:" + endDateStr + "\n" +
                    "DESCRIPTION:" + description + "\n" +
                    "END:VEVENT\n" +
                    "END:VCALENDAR";

            return icsContent.getBytes(StandardCharsets.UTF_8); // Trả về byte[] của tệp ICS
        } catch (Exception e) {
            log.warn("Cannot generate ICS file: {}", e.getMessage());
            throw new AppException(ErrorCode.CANNOT_GENERATE_ICS);
        }

    }
}
