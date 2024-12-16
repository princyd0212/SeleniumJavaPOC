package com.sam.selenium.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonFileReader {
    private static JsonNode jsonNode;

    public JsonFileReader(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found at path: " + filePath);
        }
        jsonNode = objectMapper.readTree(file);
    }

    public static JsonNode getNode(String key) {
        return jsonNode.path(key);
    }
}
