package com.sam.selenium.tests;

import com.sam.selenium.utils.ApiHelper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ApiTests {
    @Test
    public void validateGetRequest() {
        Response response = ApiHelper.sendGetRequest("https://petstore.swagger.io/v2/pet/3");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("id"));
    }

    @Test
    public void validatePostRequest() {
        String body = "{\n" +
                "  \"id\": 4546,\n" +
                "  \"category\": {\n" +
                "    \"id\": 4546,\n" +
                "    \"name\": \"Lucky\"\n" +
                "  },\n" +
                "  \"name\": \"doggie\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"string\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 4546,\n" +
                "      \"name\": \"Lucky\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"available\"\n" +
                "}";
        Response response = ApiHelper.sendPostRequest("https://petstore.swagger.io/v2/pet", body);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("id"));
    }
}
