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
        RequestSpecification request = RestAssured.given().baseUri(url);

        // Add headers
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(request::header);
        } else {
            request.header("Content-Type", "application/json"); // Ensure Content-Type is set
        }

        // Add body
        if (body != null && !body.isEmpty()) {
            request.body(body);
        }

        // Execute request
        Response response;
        switch (method.toUpperCase()) {
            case "POST":
                response = request.post();
                break;
            case "PUT":
                response = request.put();
                break;
            case "DELETE":
                response = request.delete();
                break;
            case "GET":
            default:
                response = request.get();
                break;
        }

        return response;
    }


    public static Map<String, String> jsonNodeToMap(JsonNode node) {
        Map<String, String> map = new HashMap<>();
        if (node != null && node.isObject()) {
            node.fields().forEachRemaining(entry -> map.put(entry.getKey(), entry.getValue().asText()));
        }
        return map;
    }

    public String getAuthToken(String authUrl, Map<String, String> credentials) {
        RequestSpecification request = RestAssured.given()
                .baseUri(authUrl)
                .header("Content-Type", "application/json")
                .body(new com.google.gson.Gson().toJson(credentials));

        Response response = request.post();

        if (response.getStatusCode() == 200) {
            String token = response.jsonPath().getString("token");
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("Auth token is missing in response.");
            }
            return token;
        } else {
            throw new RuntimeException("Failed to retrieve auth token. Status: " + response.getStatusCode() +
                    "\nResponse: " + response.getBody().asString());
        }
    }

}
