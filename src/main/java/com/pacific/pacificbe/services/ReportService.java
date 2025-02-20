package com.pacific.pacificbe.services;

import java.util.List;
import java.util.Map;

public interface ReportService {
	byte[] exportReport(String reportFileName, List<?> data, Map<String, Object> parameters) throws Exception;
}
