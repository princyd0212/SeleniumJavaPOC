package com.sam.selenium.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiHelper {
    private static String requestMethod; // To track the request method

    public static Response sendGetRequest(String endpoint) {
        requestMethod = "get"; // Track GET method
        return RestAssured.get(endpoint);
    }

    public static Response sendPostRequest(String endpoint, Object body) {
        requestMethod = "post"; // Track GET method
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .post(endpoint);
    }

    public static Response sendPutRequest(String endpoint, Object body) {
        requestMethod = "put"; // Track PUT method
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .put(endpoint);
    }

    public static Response sendDeleteRequest(String endpoint) {
        requestMethod = "delete"; // Track DELETE method
        return RestAssured.delete(endpoint);
    }

    // Helper method to get the request method type
    public static String getRequestType() {
        if (requestMethod == null) {
            return "unknown"; // Default if no request method has been set
        }
        return requestMethod.toLowerCase();
    }
}
