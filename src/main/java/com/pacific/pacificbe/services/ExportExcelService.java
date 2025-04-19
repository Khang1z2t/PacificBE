package com.pacific.pacificbe.services;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public interface ExportExcelService {
    <T> void exportToExcel(HttpServletResponse response, List<T> dataList, String sheetName) throws IOException;
}
