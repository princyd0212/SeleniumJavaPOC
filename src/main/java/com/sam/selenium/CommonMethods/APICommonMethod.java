package com.sam.selenium.CommonMethods;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class APICommonMethod {
    public static Response sendRequest(String method, String url, Map<String, String> headers, String body) {
        RestAssured.baseURI = url;
        RequestSpecification request = RestAssured.given();

        // Add headers dynamically
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(request::header);
        }

        // Add body dynamically
        if (body != null && !body.isEmpty()) {
            request.body(body);
        }

        // Execute the request
        switch (method.toUpperCase()) {
            case "POST":
                return request.post();
            case "PUT":
                return request.put();
            case "DELETE":
                return request.delete();
            case "GET":
            default:
                return request.get();
        }
    }

    public static Map<String, String> mergeHeadersFromJsonNodes(JsonNode defaultHeadersNode, JsonNode additionalHeadersNode) {
        // Initialize the merged headers map
        Map<String, String> mergedHeaders = new HashMap<>();

        // Helper method to add headers from a JsonNode to the map
        addHeadersToMap(defaultHeadersNode, mergedHeaders);
        addHeadersToMap(additionalHeadersNode, mergedHeaders);

        return mergedHeaders;
    }

    public static Map<String, String> jsonNodeToMap(JsonNode node) {
        Map<String, String> map = new HashMap<>();
        if (node != null && node.isObject()) {
            node.fields().forEachRemaining(entry -> map.put(entry.getKey(), entry.getValue().asText()));
        }
        return map;
    }


    private static void addHeadersToMap(JsonNode headersNode, Map<String, String> headersMap) {
        if (headersNode != null && headersNode.isObject()) {
            headersNode.fields().forEachRemaining(entry -> headersMap.put(entry.getKey(), entry.getValue().asText()));
        }
    }

    public String getAuthToken(String authUrl, Map<String, String> credentials) {
        RestAssured.baseURI = authUrl;
        RequestSpecification request = RestAssured.given();

        // Set headers
        request.header("Content-Type", "application/json");

        // Convert credentials map to JSON string
        String body = new com.google.gson.Gson().toJson(credentials);
        request.body(body);

        // Send POST request
        Response response = request.post();

        // Check the response and extract token
        if (response.getStatusCode() == 200) {
            // Extract token from JSON response (assuming token field is named "token")
            return response.jsonPath().getString("token");
        } else {
            throw new RuntimeException("Failed to retrieve auth token. Status code: " + response.getStatusCode() +
                    "\nResponse: " + response.getBody().asString());
        }
    }

}
