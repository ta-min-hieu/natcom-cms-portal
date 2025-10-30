package com.ringme.common;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Log4j2
@Component
public class ExportExcel {

    public void export(List<?> objects, String[] headers, HttpServletResponse response){
        try {
                long startTime = System.currentTimeMillis();
                writeExcel(objects, headers, response);
                long endTime = System.currentTimeMillis();
                long timeExport = endTime - startTime;
                log.info("Time Export|" + timeExport);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
    public void exportSDK(List<?> objects, String[] headers, HttpServletResponse response){
        try {
            long startTime = System.currentTimeMillis();
            writeExcelSDK(objects, headers, response);
            long endTime = System.currentTimeMillis();
            long timeExport = endTime - startTime;
            log.info("Time Export SDK|" + timeExport);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    public void exportDetailChatToQueue(List<?> objects, String[] headers, HttpServletResponse response){
        try {
            long startTime = System.currentTimeMillis();
            writeExcelDetailChatToQueue(objects, headers, response);
            long endTime = System.currentTimeMillis();
            long timeExport = endTime - startTime;
            log.info("Time Export SDK|" + timeExport);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
    private static void writeExcel(List<?> objects, String[] headers, HttpServletResponse response) {
        try {
            // Create Workbook
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            // Create sheet
            SXSSFSheet sheet = workbook.createSheet("ReportSDK"); // Create sheet with sheet name
            int rowIndex = 0;
            // Write header
            writeHeader(sheet, rowIndex, headers);
            // Write data
            rowIndex++;
            for (Object object : objects) {
                // Create row
                SXSSFRow row = sheet.createRow(rowIndex);
                // Write data on row
                writeObject(object, row);
                rowIndex++;
            }

            for (int i=0; i<headers.length; i++) {
                sheet.trackAllColumnsForAutoSizing();
                sheet.autoSizeColumn(i);
            }

            // Create file excel
            createOutputFile(workbook, response);
            log.info("Done!!!");
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
    // Write header with format
    private static void writeHeader(SXSSFSheet sheet, int rowIndex, String[] headers) {
        // Create row
        SXSSFRow row = sheet.createRow(rowIndex);
        // Create cells
        SXSSFCell cell;
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
        }
    }
    // Write data
    private static void writeObject(Object object, SXSSFRow row) {
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                Field field = fields[i];
                field.setAccessible(true);
                Object value = field.get(object);

                SXSSFCell cell = row.createCell(i);
                cell.setCellValue(value != null ? value.toString() : "");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    // Create output file
    private static void createOutputFile(SXSSFWorkbook workbook, HttpServletResponse response) {
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();

            outputStream.close();
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }

    public HttpServletResponse setResponse(HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=report_sdk" + currentDateTime + ".xlsx";
            response.setHeader(headerKey, headerValue);
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
        return response;
    }
    private static void writeExcelSDK(List<?> objects, String[] headers, HttpServletResponse response) {
        try {
            // Create Workbook
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            // Create sheet
            SXSSFSheet sheet = workbook.createSheet("ReportSDK"); // Create sheet with sheet name
            int rowIndex = 0;
            //writeHeaderCustom
            writeHeaderCustom(sheet, rowIndex, headers);
            rowIndex = rowIndex + 2;
            // Write header
            writeHeader(sheet, rowIndex, headers);
            // Write data
            rowIndex++;
            for (Object object : objects) {
                // Create row
                SXSSFRow row = sheet.createRow(rowIndex);
                // Write data on row
                writeObject(object, row);
                rowIndex++;
            }
            // Create file excel
            createOutputFile(workbook, response);
            log.info("Done!!!");
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
    private static void writeExcelDetailChatToQueue(List<?> objects, String[] headers, HttpServletResponse response) {
        try {
            // Create Workbook
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            // Create sheet
            SXSSFSheet sheet = workbook.createSheet("ReportSDK"); // Create sheet with sheet name
            int rowIndex = 0;
            //writeHeaderCustom
            writeHeaderCustomDetailChatToQueue(sheet, rowIndex, headers);
            rowIndex = rowIndex + 2;
            // Write header
            writeHeader(sheet, rowIndex, headers);
            // Write data
            rowIndex++;
            for (Object object : objects) {
                // Create row
                SXSSFRow row = sheet.createRow(rowIndex);
                // Write data on row
                writeObject(object, row);
                rowIndex++;
            }

            for (int i=0; i<headers.length; i++) {
                sheet.trackAllColumnsForAutoSizing();
                sheet.autoSizeColumn(i);
            }

            // Create file excel
            createOutputFile(workbook, response);
            log.info("Done!!!");
        } catch (Exception e) {
            log.error("ERROR|" + e.getMessage(), e);
        }
    }
    // Write header SDK
    public static void writeHeaderCustom(SXSSFSheet sheet, int rowIndex, String[] headers) {
        // Create row
        SXSSFRow row = sheet.createRow(rowIndex);
        // Create cells
        SXSSFCell cell;
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            if (i == 0) cell.setCellValue("Mã BĐT");
            if (i == 1) cell.setCellValue("Tên BĐT");
            if (i == 2) cell.setCellValue("Mã Bưu Cục");
            if (i == 3) cell.setCellValue("Số liệu đầu KỲ báo cáo");
            if (i == 30) cell.setCellValue("SỐ LIỆU PHÁT SINH MỚI TRONG KỲ BÁO CÁO");
            if (i == 57) cell.setCellValue("SỐ LIỆU LUỸ KẾ CUỐI KỲ BÁO CÁO");
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 29));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 30, 56));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 57, 83));
        row = sheet.createRow(rowIndex + 1);
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            if (i == 3) cell.setCellValue("Số lượng account đã mở");
            if (i == 9) cell.setCellValue("Số lượng account phát sinh chat");
            if (i == 16) cell.setCellValue("Chat KH vs Bưu tá (DD)");
            if (i == 18) cell.setCellValue("SL phiên chat KH với bưu cục (PNS)");
            if (i == 24) cell.setCellValue("SL phiên Chat KH với đầu mối CSKH (One CX)");
            if (i == 27) cell.setCellValue("SL phiên Chat KH với đầu mối bán hàng (CCP)");
            if (i == 30) cell.setCellValue("Số lượng account đã mở");
            if (i == 36) cell.setCellValue("Số lượng account phát sinh chat");
            if (i == 43) cell.setCellValue("Chat KH vs Bưu tá (DD)");
            if (i == 45) cell.setCellValue("SL phiên chat KH với bưu cục (PNS)");
            if (i == 51) cell.setCellValue("SL phiên Chat KH với đầu mối CSKH (One CX)");
            if (i == 54) cell.setCellValue("SL phiên Chat KH với đầu mối bán hàng (CCP)");
            if (i == 57) cell.setCellValue("Số lượng account đã mở");
            if (i == 63) cell.setCellValue("Số lượng account phát sinh chat");
            if (i == 70) cell.setCellValue("Chat KH vs Bưu tá (DD)");
            if (i == 73) cell.setCellValue("SL phiên chat KH với bưu cục (PNS)");
            if (i == 79) cell.setCellValue("SL phiên Chat KH với đầu mối CSKH (One CX)");
            if (i == 82) cell.setCellValue("SL phiên Chat KH với đầu mối bán hàng (CCP)");
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 8));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 15));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 16, 17));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 18, 23));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 24, 26));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 27, 29));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 30, 35));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 36, 42));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 43, 44));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 45, 50));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 51, 53));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 54, 56));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 57, 62));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 63, 69));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 70, 72));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 73, 78));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 79, 81));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 82, 83));
    }

    public static void writeHeaderCustomDetailChatToQueue(SXSSFSheet sheet, int rowIndex, String[] headers) {
        // Create row
        SXSSFRow row = sheet.createRow(rowIndex + 1);
        // Create cells
        SXSSFCell cell;
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            if (i == 0) cell.setCellValue("Thời gian gửi");
            if (i == 1) cell.setCellValue("Thông tin gửi");
            if (i == 8) cell.setCellValue("Thông tin nhận");
            if (i == 14) cell.setCellValue("Số hiệu bưu gửi");
            if (i == 15) cell.setCellValue("Mã cuộc chat");
            if (i == 16) cell.setCellValue("Nội dung cuộc chat");
            if (i == 17) cell.setCellValue("Trạng thái đọc tin chat");
        }
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 7));
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 8, 13));
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 17, 17));
    }
}