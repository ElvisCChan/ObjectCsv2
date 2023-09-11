package com.test.main;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVtoXLSX {
    public static void main(String[] args) {
        try {
            String[] csvFiles = {"output.csv", "output2.csv"}; // Replace with your CSV file names
            String xlsxFile = "output.xlsx"; // Replace with your desired output file name
            String pdfFile = "output.pdf"; // Replace with your desired PDF file name

            // Step 1: Group all CSV files into a single XLSX file
            Workbook workbook = new XSSFWorkbook();
            for (String csvFile : csvFiles) {
                String sheetName = csvFile.substring(0, csvFile.lastIndexOf("."));
                Sheet sheet = workbook.createSheet(sheetName);
                List<String[]> rows = readCSV(csvFile);
                int rowNum = 0;
                for (String[] rowData : rows) {
                    Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    for (String cellData : rowData) {
                        Cell cell = row.createCell(colNum++);
                        cell.setCellValue(cellData);
                    }
                }
            }

            // Step 2: Change header style for each sheet
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            for (Sheet sheet : workbook) {
                Row headerRow = sheet.getRow(0);
                for (Cell headerCell : headerRow) {
                    headerCell.setCellStyle(headerStyle);
                }
            }

            // Step 3: Change style for columns with name starting with "input."
            CellStyle inputColumnStyle = workbook.createCellStyle();
            inputColumnStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            inputColumnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            for (Sheet sheet : workbook) {
                Row headerRow = sheet.getRow(0);
                for (Cell headerCell : headerRow) {
                    if (headerCell.getStringCellValue().startsWith("input.")) {
                        headerCell.setCellStyle(inputColumnStyle);
                    }
                }
            }

            // Step 4: Auto-fit column widths with content
            for (Sheet sheet : workbook) {
                for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // Step 5: Resize the ratio to 80%
            for (Sheet sheet : workbook) {
                // sheet.setZoom(4, 5); // 80% zoom
                sheet.setZoom(110);
            }

            // Step 6: Export the result to PDF format
            FileOutputStream pdfOutputStream = new FileOutputStream(pdfFile);
            for (Sheet sheet : workbook) {
                sheet.setFitToPage(true);
                sheet.setHorizontallyCenter(true);
                // printToPDF(sheet, pdfOutputStream);
            }
            pdfOutputStream.close();

            // Save the workbook as XLSX file
            FileOutputStream xlsxOutputStream = new FileOutputStream(xlsxFile);
            workbook.write(xlsxOutputStream);
            xlsxOutputStream.close();

            workbook.close();
            System.out.println("CSV to XLSX conversion successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String[]> readCSV(String csvFile) throws IOException {
        List<String[]> rows = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].replaceAll("^\"|\"$", ""); // Remove double quotes
            }
            rows.add(data);
        }
        reader.close();
        return rows;
    }


}
