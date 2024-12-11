package com.sam.selenium.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiHelper {
    public static Response sendGetRequest(String endpoint) {
        return RestAssured.get(endpoint);
    }

    public static Response sendPostRequest(String endpoint, Object body) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .post(endpoint);
    }

    public static Response sendPutRequest(String endpoint, Object body) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .put(endpoint);
    }

    public static Response sendDeleteRequest(String endpoint) {
        return RestAssured.delete(endpoint);
    }
}
