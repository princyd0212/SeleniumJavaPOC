package com.sam.selenium.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.sam.selenium.CommonMethods.APICommonMethod;
import com.sam.selenium.utils.JsonFileReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;
import java.io.IOException;


public class ApiTests extends APICommonMethod {

    @BeforeClass
    public void setup() throws IOException {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\config\\apitestdata.json";
        new JsonFileReader(filePath);
    }


    @Test
    public void APIRequest() {
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

        // Fetch authentication token
        String authUrl = JsonFileReader.getNode("login").path("url").asText();
        Map<String, String> credentials = APICommonMethod.jsonNodeToMap(JsonFileReader.getNode("login").path("credentials"));
        String authToken = getAuthToken(authUrl, credentials);
        System.out.println("Auth Token: " + authToken);

        // Merge headers or use default headers if additional headers are not present
        Map<String, String> headers = additionalHeadersNode != null
                ? APICommonMethod.mergeHeadersFromJsonNodes(defaultHeadersNode, additionalHeadersNode)
                : APICommonMethod.jsonNodeToMap(defaultHeadersNode);

//        headers.put("Authorization", "Bearer " + authToken);

        System.out.println("Merged Headers: " + headers);


        // Send the POST request
        Response response = APICommonMethod.sendRequest("GET", apiUrl, headers, body);
//        Response response = ApiHelper.sendPostRequest(apiUrl, body);

        // Debugging: Log the actual response status code and body
        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());

        // Assert the status code and assert value in the response body
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status Code mismatch!");
        Assert.assertTrue(response.getBody().asString().contains(assertValue), "Response does not contain the expected value!");
    }


}
