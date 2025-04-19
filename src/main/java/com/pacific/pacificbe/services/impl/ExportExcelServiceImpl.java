package com.pacific.pacificbe.services.impl;

import com.pacific.pacificbe.services.ExportExcelService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExportExcelServiceImpl implements ExportExcelService {

    @Override
    public <T> void exportToExcel(HttpServletResponse response, List<T> dataList, String sheetName) throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("Danh sách dữ liệu trống!");
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        // Lấy class của đối tượng trong danh sách
        Class<?> clazz = dataList.getFirst().getClass();
        Field[] fields = clazz.getDeclaredFields();

        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < fields.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(fields[i].getName()); // Lấy tên thuộc tính làm tiêu đề
        }

        // Đổ dữ liệu vào các dòng
        int rowNum = 1;
        for (T item : dataList) {
            Row row = sheet.createRow(rowNum++);
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true); // Cho phép truy cập vào private fields
                try {
                    Object value = fields[i].get(item);
                    row.createCell(i).setCellValue(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    row.createCell(i).setCellValue("Lỗi truy cập dữ liệu");
                }
            }
        }

        // Xuất file Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + sheetName + ".xlsx");

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
