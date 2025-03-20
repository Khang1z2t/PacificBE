package com.pacific.pacificbe.services.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.pacific.pacificbe.services.ReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements ReportService{
    private JdbcTemplate jdbcTemplate;

	@Override
	public byte[] exportReport(String reportFileName, List<?> data, Map<String, Object> parameters) throws Exception{
        // Load file .jasper từ thư mục resources
        InputStream reportStream = new ClassPathResource("reportJasper/" + reportFileName + ".jasper").getInputStream();

        // Tạo nguồn dữ liệu cho báo cáo
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        // Điền dữ liệu vào báo cáo
        JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parameters, dataSource);

        // Xuất ra file PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
	}

    @Override
    public byte[] exportBookingReport(String year, String bookStatus) throws Exception {
        // Load file báo cáo từ thư mục resources/reports/
        InputStream reportStream = new ClassPathResource("reports/booking_report.jasper").getInputStream();
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(reportStream);

        // Truy vấn dữ liệu từ database
        String sql = """ 
                SELECT MONTH(b.created_at) AS bookingMonth,
                SUM(b.total_amount) AS totalRevenue
                FROM booking b
                WHERE YEAR(b.created_at) = ?
                AND (? IS NULL OR LOWER(b.booking_status) LIKE LOWER(CONCAT('%', ?, '%')))
                GROUP BY MONTH(b.created_at), YEAR(b.created_at), b.booking_status
                ORDER BY bookingMonth
                """;

        List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, year, bookStatus, bookStatus);

        // Chuyển dữ liệu sang JasperReports
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ReportTitle", "Booking Revenue Report");
        parameters.put("Year", year);
        parameters.put("BookingStatus", bookStatus);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Xuất báo cáo ra PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

}
