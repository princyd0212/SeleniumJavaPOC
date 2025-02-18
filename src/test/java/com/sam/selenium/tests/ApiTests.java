package com.sam.selenium.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.selenium.CommonMethods.APICommonMethod;
import com.sam.selenium.utils.JsonFileReader;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;


public class ApiTests extends APICommonMethod {

    private String authToken;
    private String getAllProductsUrl, productDetailsUrl, addToCartUrl, checkoutUrl;
    private String requestBody;
    private Map<String, String> headers;
    private JsonNode getAllProductsNode, productDetailsNode, addToCartNode, checkoutNode;
    //private String requestHeaders ;

    @BeforeClass
    public void setup() throws IOException {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\java\\resources\\config\\apitestdata.json";
        JsonFileReader.loadJson(filePath);
        headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
    }

    @Test
    public void LoginAPI() throws JsonProcessingException {

        JsonNode loginNode = JsonFileReader.getNode("login");
        String authUrl = loginNode.path("url").asText();
        Map<String, String> credentials = APICommonMethod.jsonNodeToMap(loginNode.path("credentials"));
        authToken = new APICommonMethod().getAuthToken(authUrl, credentials);
        headers.put("Authorization", authToken);
        int expectedStatusCode = loginNode.path("response").path("statusCode").asInt();
        Map<String, String> requestHeaders = new HashMap<>(headers);
        requestHeaders.put("Accept", "application/json");
        requestBody = new ObjectMapper().writeValueAsString(credentials);
        Response response = APICommonMethod.sendRequest("POST", authUrl, headers, requestBody);
        int actualStatusCode = response.getStatusCode();

        System.out.println("Actual Status Code:- : " + actualStatusCode);
        System.out.println("Expected Status Code:-" + expectedStatusCode);
        System.out.println("Response Body: " + response.getBody().asString());
        System.out.println("Request URL:- " + authUrl);
        System.out.println("Credentials Map: " + credentials);
        System.out.println("Request Body: " + requestBody);
        System.out.println("Auth Token: " + authToken);
        Assert.assertNotNull(authToken, "Auth Token should not be null!");
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status Code mismatch!");

        /// Product Code

        getAllProductsNode = JsonFileReader.getNode("getAllProducts");
        getAllProductsUrl = getAllProductsNode.path("url").asText();
        System.out.println("Get all Product URL :- " + getAllProductsUrl);

        //product Details
        productDetailsNode = JsonFileReader.getNode("Productdeatils");
        productDetailsUrl = productDetailsNode.path("url").asText();
        System.out.println("Product Details URL: " + productDetailsUrl);

        //Add to cart
        addToCartNode = JsonFileReader.getNode("addToCart");
        addToCartUrl = addToCartNode.path("url").asText();
        System.out.println("Add To Cart URL: " + addToCartUrl);

        //Checkout product
        checkoutNode = JsonFileReader.getNode("checkout");
        checkoutUrl = checkoutNode.path("url").asText();
        System.out.println("Checkout URL: " + checkoutUrl);

    }

    @Test(dependsOnMethods = "LoginAPI")  //  Ensures Login runs first
    public void ProductAPI() throws JsonProcessingException {
        String productrequestBody = new ObjectMapper().writeValueAsString(getAllProductsNode.path("body"));
        System.out.println("Product Body:- " + productrequestBody);
        Response getAllProductsResponse = APICommonMethod.sendRequest("POST", getAllProductsUrl, headers, productrequestBody);
        System.out.println("Get All Products Response Body: " + getAllProductsResponse.getBody().asString());
    }

    @Test(dependsOnMethods = "LoginAPI")
    public void ProductDetailsAPI() {
        Response productDetailsResponse = APICommonMethod.sendRequest("GET", productDetailsUrl, headers, requestBody);

        System.out.println("Product Details Response Body: " + productDetailsResponse.getBody().asString());

        // Validate response status code
        int actualStatusCode = productDetailsResponse.getStatusCode();
        int expectedStatusCode = productDetailsNode.path("response").path("statusCode").asInt();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status Code mismatch in ProductDetailsAPI!");

    }

    @Test(dependsOnMethods = "ProductDetailsAPI")
    public void AddToCartAPI() throws JsonProcessingException {
        // Extract correct request body for Add to Cart API
        String addToCartRequestBody = new ObjectMapper().writeValueAsString(addToCartNode.path("body"));
        System.out.println("Add to Cart Request Body: " + addToCartRequestBody);

        Response addToCartResponse = APICommonMethod.sendRequest("POST", addToCartUrl, headers, addToCartRequestBody);

        int expectedStatusCode = addToCartNode.path("response").path("statusCode").asInt();
        Assert.assertEquals(addToCartResponse.getStatusCode(), expectedStatusCode, "Status Code mismatch in AddToCartAPI!");
    }
    @Test(dependsOnMethods = "AddToCartAPI")
    public void CheckoutAPI() throws JsonProcessingException{
        String CheckoutRequestBody = new ObjectMapper().writeValueAsString(checkoutNode.path("body"));
        System.out.println("Add to Cart Request Body: " + CheckoutRequestBody);

        Response CheckoutResponse = APICommonMethod.sendRequest("POST", checkoutUrl, headers, CheckoutRequestBody);

        int expectedStatusCode = checkoutNode.path("response").path("statusCode").asInt();
        Assert.assertEquals(CheckoutResponse.getStatusCode(), expectedStatusCode, "Status Code mismatch in ChekoutAPI!");

    }
}
