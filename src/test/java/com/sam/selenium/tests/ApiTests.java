package com.sam.selenium.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.sam.selenium.CommonMethods.APICommonMethod;
import com.sam.selenium.utils.ApiHelper;
import com.sam.selenium.utils.JsonFileReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;


public class ApiTests extends APICommonMethod {

    @BeforeClass
    public void setup() throws IOException {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\config\\apitestdata.json";
        new JsonFileReader(filePath);
    }


    @Test
    public void validatePostRequest() {
        // Get data from the JSON file
        JsonNode postNode = JsonFileReader.getNode("post");
        String apiUrl = postNode.path("url").asText();
        String body = postNode.path("body").toString(); // Convert JSON object to String
        int expectedStatusCode = postNode.path("response").path("statusCode").asInt();
        String assertValue = postNode.path("response").path("assertValue").asText();
        JsonNode defaultHeadersNode = JsonFileReader.getNode("headers");
        JsonNode additionalHeadersNode = JsonFileReader.getNode("additional headers");

        // Log for debugging
        System.out.println("URL: " + apiUrl);
        System.out.println("Request Body: " + body);
        System.out.println("Expected Status Code: " + expectedStatusCode);

        // Merge headers or use default headers if additional headers are not present
        Map<String, String> headers = additionalHeadersNode != null
                ? APICommonMethod.mergeHeadersFromJsonNodes(defaultHeadersNode, additionalHeadersNode)
                : APICommonMethod.jsonNodeToMap(defaultHeadersNode);

        System.out.println("Merged Headers: " + headers);

        // Send the POST request
        Response response = APICommonMethod.sendRequest("POST", apiUrl, headers, body);
//        Response response = ApiHelper.sendPostRequest(apiUrl, body);

        // Debugging: Log the actual response status code and body
        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert the status code and assert value in the response body
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status Code mismatch!");
        Assert.assertTrue(response.getBody().asString().contains(assertValue), "Response does not contain the expected value!");
    }

    @Test
    public void validateGetRequest() {
        // Get data from the JSON file
        JsonNode getNode = JsonFileReader.getNode("get");
        String apiUrl = getNode.path("url").asText();
        int expectedStatusCode = getNode.path("response").path("statusCode").asInt();
        String assertValue = getNode.path("response").path("assertValue").asText();
        // Dynamic headers
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
//        headers.put("Authorization", "Bearer your_token_here");
        // Log for debugging
        System.out.println("URL: " + apiUrl);
        System.out.println("Expected Status Code: " + expectedStatusCode);

//        // Send the GET request
        Response response = APICommonMethod.sendRequest("GET", apiUrl, headers, null);
//        Response response = ApiHelper.sendGetRequest(url);

        // Debugging: Log the actual response status code and body
        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert the status code and assert value in the response body
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status Code mismatch!");
        Assert.assertTrue(response.getBody().asString().contains(assertValue), "Response does not contain the expected value!");
    }

    @Test
    public void validatePutRequest() {
        // Get data from the JSON file
        JsonNode putNode = JsonFileReader.getNode("put");
        String apiUrl = putNode.path("url").asText();
        String body = putNode.path("body").toString(); // Convert JSON object to String
        int expectedStatusCode = putNode.path("response").path("statusCode").asInt();
        String assertValue = putNode.path("response").path("assertValue").asText();

        // Log for debugging
        System.out.println("URL: " + apiUrl);
        System.out.println("Request Body: " + body);
        System.out.println("Expected Status Code: " + expectedStatusCode);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
//        headers.put("Authorization", "Bearer your_token_here");

        // Send the PUT request
        Response response = APICommonMethod.sendRequest("PUT", apiUrl, headers, body);
//        Response response = ApiHelper.sendPutRequest(apiUrl, body);

        // Debugging: Log the actual response status code and body
        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert the status code and assert value in the response body
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status Code mismatch!");
        Assert.assertTrue(response.getBody().asString().contains(assertValue), "Response does not contain the expected value!");
    }

    @Test
    public void validateDeleteRequest() {
        // Get data from the JSON file
        JsonNode deleteNode = JsonFileReader.getNode("delete");
        String apiUrl = deleteNode.path("url").asText();
        int expectedStatusCode = deleteNode.path("response").path("statusCode").asInt();

        // Log for debugging
        System.out.println("URL: " + apiUrl);
        System.out.println("Expected Status Code: " + expectedStatusCode);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
//        headers.put("Authorization", "Bearer your_token_here");

        // Send the DELETE request
        Response response = APICommonMethod.sendRequest("DELETE", apiUrl, headers, null);
//        Response response = ApiHelper.sendDeleteRequest(apiUrl);

        // Debugging: Log the actual response status code and body
        System.out.println("Actual Status Code: " + response.getStatusCode());

        // Assert the status code and assert value in the response body
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status Code mismatch!");
    }
    @Test
    public void testBasicAuth() {
        String endpoint = "https://api.example.com/resource";
        String username = "testUser";
        String password = "testPassword";

        Response response = ApiHelper.getWithBasicAuth(endpoint, username, password);
        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @Test
    public void testBearerTokenAuth() {
        String endpoint = "https://api.example.com/resource";
        String token = "yourAccessToken";

        Response response = ApiHelper.getWithBearerToken(endpoint, token);
        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @Test
    public void testApiKeyAuth() {
        String endpoint = "https://api.example.com/resource";
        String apiKey = "yourApiKey";

        Response response = ApiHelper.getWithApiKey(endpoint, apiKey);
        Assert.assertEquals(response.getStatusCode(), 200);
        System.out.println("Response Body: " + response.getBody().asString());
    }



}
