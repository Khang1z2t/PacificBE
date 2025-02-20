package com.pacific.pacificbe.services.impl;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.pacific.pacificbe.services.ReportService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ReportServiceImpl implements ReportService{

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

}
