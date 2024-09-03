package com.sam.selenium.DataHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadCSVData {
    public static List<Map<String, String>> readCSVData(String filePathcsv) throws IOException {
        List<Map<String, String>> csvData = new ArrayList<>();
        System.out.println("---- CsvFile Read Data ----");
        try (BufferedReader br = new BufferedReader(new FileReader(filePathcsv))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                throw new IOException("CSV file is empty.");
            }

            String[] headers = headerLine.split(",");

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] values = line.split(",");
                Map<String, String> rowData = new HashMap<>();
                for (int i = 0; i < values.length; i++) {
                   // rowData.put(headers[i], i < values.length ? values[i].trim() : "");
                    rowData.put(headers[i], values[i].trim());
                }

                if (rowData.values().stream().anyMatch(v -> !v.isEmpty())) {
                    csvData.add(rowData);

                }
            }
        }
        return csvData;
    }
}
