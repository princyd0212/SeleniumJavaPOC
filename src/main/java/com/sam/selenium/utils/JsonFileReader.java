package com.sam.selenium.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileReader {
    private static JsonNode jsonNode; // Static variable to store JSON data

    // Load JSON file method (static)
    public static void loadJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("File not found at path: " + filePath);
        }

        jsonNode = objectMapper.readTree(file);
        System.out.println("JSON file loaded successfully.");
    }

    // Get JSON node safely
    public static JsonNode getNode(String key) {
        if (jsonNode == null) {
            throw new IllegalStateException("JSON data is not initialized. Call loadJson() first.");
        }
        return jsonNode.path(key);
    }
}
