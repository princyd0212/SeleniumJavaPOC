package com.sam.selenium.stepDefinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.sam.selenium.base.BaseTest;
import io.cucumber.java.en.When;
import com.sam.selenium.utils.ApiHelper;
import com.sam.selenium.utils.JsonFileReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.cucumber.java.Before;
import org.testng.Assert;

import java.io.IOException;

public class ApiTestStepDefination extends BaseTest {
    private Response response;
    private JsonFileReader jsonFileReader; // Instance variable

    @Before
    public void setup() throws IOException {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\config\\apitestdata.json";
        jsonFileReader = new JsonFileReader(filePath); // Initialize instance
    }

    @Given("I have the API data for {string}")
    public void iHaveTheAPIDataFor(String requestType) {
        System.out.println("Fetching data for request type: " + requestType);
        JsonNode requestData = JsonFileReader.getNode(requestType); // Use instance method
        Assert.assertNotNull(requestData, "Request data not found in JSON file!");
    }

    @When("I send a POST request")
    public void iSendAPOSTRequest() {
        System.out.println("Sending POST request");
        JsonNode postNode = JsonFileReader.getNode("post");
        String url = postNode.path("url").asText();
        String body = postNode.path("body").toString();
        System.out.println("Request Body: " + body);
        response = ApiHelper.sendPostRequest(url, body);
    }

    @When("I send a GET request")
    public void iSendAGETRequest() {
        System.out.println("Sending GET request");
        JsonNode postNode = JsonFileReader.getNode("get");
        String url = postNode.path("url").asText();
        response = ApiHelper.sendGetRequest(url);
    }

    @When("I send a PUT request")
    public void iSendAPUTRequest() {
        System.out.println("Sending PUT request");
        JsonNode postNode = JsonFileReader.getNode("put");
        String url = postNode.path("url").asText();
        String body = postNode.path("body").toString();
        System.out.println("URL: " + url);
        System.out.println("Request Body: " + body);
        response = ApiHelper.sendPutRequest(url, body);
        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    @When("I send a DELETE request")
    public void iSendADELETERequest() {
        System.out.println("Sending DELETE request");
        JsonNode postNode = JsonFileReader.getNode("delete");
        String url = postNode.path("url").asText();
        response = ApiHelper.sendDeleteRequest(url);
    }

    @Then("the response status code should be from JSON")
    public void theResponseStatusCodeShouldBeFromJson() {
        String requestType = ApiHelper.getRequestType(); // E.g., "post", "get", etc.
        JsonNode requestData = JsonFileReader.getNode(requestType);
        int expectedStatusCode = requestData.path("response").path("statusCode").asInt();
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode, "Status code mismatch!");
    }

    @Then("the response body should contain from JSON")
    public void theResponseBodyShouldContainFromJson() {
        String requestType = ApiHelper.getRequestType(); // This should be a method to identify the request type (POST, GET, etc.)
        JsonNode requestData = JsonFileReader.getNode(requestType);
        String assertValue = requestData.path("response").path("assertValue").asText();
        Assert.assertTrue(response.getBody().asString().contains(assertValue),"Response does not contain the expected value!");
    }
}
