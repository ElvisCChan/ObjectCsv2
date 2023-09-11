package com.test.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CSV2XLSXUtils {

    public static void convertCsvToXlsx(String CSV_DIR) throws FileNotFoundException, IOException {
        // File dir = new File("D:\\repos\\main\\");
        File dir = new File(CSV_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        Workbook workbook = new XSSFWorkbook();
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle inputStyle = createInputStyle(workbook);
        CellStyle outputStyle = createOutputStyle(workbook);
        CellStyle invalidStyle = createInvalidStyle(workbook);
        for (File file : files) {
            Sheet sheet = workbook.createSheet(file.getName());
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int rowNumber = 0;
                while ((line = br.readLine()) != null) {
                    String[] columns = line.split(",");
                    for (int i = 0; i < columns.length; i++) {
                        columns[i] = columns[i].replaceAll("^\"|\"$", ""); // Remove double quotes
                    }
                    Row row = sheet.createRow(rowNumber++);
                    for (int i = 0; i < columns.length; i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellValue(columns[i]);
                        if (rowNumber == 1) {
                            cell.setCellStyle(headerStyle);
                        }
                    }
                }
            }
            for (int i = 0; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
                sheet.autoSizeColumn(i);
            }
            sheet.setZoom(80); 
            int startRowIndex = sheet.getFirstRowNum(); 
            int endRowIndex = sheet.getLastRowNum();
            Row headerRow = sheet.getRow(startRowIndex);
            for (Cell cell : headerRow) {
                String headerValue = cell.getStringCellValue();
                if (headerValue.startsWith("input.")) {
                    int colInx = cell.getColumnIndex();
                    for (int i = startRowIndex + 1; i <= endRowIndex; i++) {
                        Row row = sheet.getRow(i);
                        Cell inputCell = row.getCell(colInx);
                        inputCell.setCellStyle(inputStyle);
                    }
                } else {
                    int colInx = cell.getColumnIndex();
                    for (int i = startRowIndex + 1; i <= endRowIndex; i++) {
                        Row row = sheet.getRow(i);
                        Cell outputputCell = row.getCell(colInx);
                        outputputCell.setCellStyle(outputStyle);
                    }
                }
            }

            int lastColumnIndex = -1;
            for (Row row : sheet) {
                int currentLastCellIndex = row.getLastCellNum() - 1;
                if (currentLastCellIndex > lastColumnIndex) {
                    lastColumnIndex = currentLastCellIndex;
                }

                for (Cell cell : row) {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.contains("invalid")) {
                        cell.setCellStyle(invalidStyle);
                        int columnIndex = cell.getColumnIndex();
                        CellRangeAddress columnRange = new CellRangeAddress(row.getRowNum(), row.getRowNum(),
                                columnIndex, currentLastCellIndex);
                        sheet.addMergedRegion(columnRange);
                    }
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream("output.xlsx")) {
            workbook.write(fos);
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setFontName("Arial");
        style.setFont(headerFont);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createInputStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setItalic(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createOutputStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setItalic(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private static CellStyle createInvalidStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setItalic(true);
        font.setColor(IndexedColors.RED.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }
}