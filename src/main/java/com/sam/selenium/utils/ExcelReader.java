package com.sam.selenium.utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelReader {
    private Map<String, String> dataMap = new HashMap<>();

    public ExcelReader(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet("Sheet1");
            DataFormatter dataFormatter = new DataFormatter();

            for (Row row : sheet) {
                String key = dataFormatter.formatCellValue(row.getCell(0));
                String value = dataFormatter.formatCellValue(row.getCell(1));
                dataMap.put(key, value);
            }
        }
    }

    public String getCellData(String key) {
        return dataMap.get(key);
    }
}
