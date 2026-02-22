package com.automation.utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtil {

    public static Object[][] getTestData(String excelPath, String sheetName) {
        Object[][] data = null;
        try {
            FileInputStream fis = new FileInputStream(excelPath);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);
            XSSFSheet sheet = workbook.getSheet(sheetName);

            // Get row and column counts
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getLastCellNum();

            // Initialize the array (excluding the header row, so rowCount - 1)
            data = new Object[rowCount - 1][colCount];
            DataFormatter formatter = new DataFormatter();

            // Loop through rows and columns to populate the array
            for (int i = 1; i < rowCount; i++) { // Start at 1 to skip header
                for (int j = 0; j < colCount; j++) {
                    // DataFormatter ensures numbers/text are all read as Strings
                    data[i - 1][j] = formatter.formatCellValue(sheet.getRow(i).getCell(j));
                }
            }
            workbook.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}