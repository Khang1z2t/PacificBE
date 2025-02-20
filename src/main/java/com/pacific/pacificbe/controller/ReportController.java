package com.pacific.pacificbe.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pacific.pacificbe.services.ReportService;
import com.pacific.pacificbe.utils.UrlMapping;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping(UrlMapping.EXPORT)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportservice;

    @GetMapping(UrlMapping.EXPORT_PDF)
    public ResponseEntity<byte[]> exportReport(@RequestParam String reportName) {
        try {
            // Lấy dữ liệu từ DB (giả sử có service lấy dữ liệu)
            List<?> data = List.of(); // Thay bằng dữ liệu thực tế từ database

            // Thêm tham số nếu cần
            Map<String, Object> parameters = new HashMap();
            parameters.put("ReportTitle", "Báo cáo sản phẩm");

            byte[] pdfReport = reportservice.exportReport(reportName, data, parameters);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reportName + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfReport);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
