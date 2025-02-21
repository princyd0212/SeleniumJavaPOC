package com.sam.selenium.stepDefinations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sam.selenium.CommonMethods.APICommonMethod;
import com.sam.selenium.utils.JsonFileReader;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.sam.selenium.base.BaseTest.driver;

public class ApiStepDefinitions {
    private final APICommonMethod apiCommonMethod = new APICommonMethod();
    private String authToken;
    private Map<String, String> headers = new HashMap<>();
    private JsonNode getAllProductsNode, productDetailsNode, addToCartNode, checkoutNode;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String getAllProductsUrl, productDetailsUrl, addToCartUrl, checkoutUrl;
    private String productRequestBody, addToCartRequestBody, checkoutRequestBody, requestBody;

    @Given("I authenticate via API")
    public void iAuthenticateViaAPI() throws IOException {
        String filePath = System.getProperty("user.dir") + "/src/test/java/resources/config/apitestdata.json";
        JsonFileReader.loadJson(filePath);

        headers.put("Content-Type", "application/json");

        JsonNode loginNode = JsonFileReader.getNode("login");
        String authUrl = loginNode.path("url").asText();
        Map<String, String> credentials = APICommonMethod.jsonNodeToMap(loginNode.path("credentials"));

        authToken = apiCommonMethod.getAuthToken(authUrl, credentials);
        headers.put("Authorization", authToken);

        Response response = APICommonMethod.sendRequest("POST", authUrl, headers, objectMapper.writeValueAsString(credentials));

        Assert.assertNotNull(authToken, "Auth Token should not be null!");
        Assert.assertEquals(response.getStatusCode(), loginNode.path("response").path("statusCode").asInt(), "Login failed!");

    }

    @When("I request all products")
    public void iRequestAllProducts() throws IOException {
        getAllProductsNode = JsonFileReader.getNode("getAllProducts");
        getAllProductsUrl = getAllProductsNode.path("url").asText();
        System.out.println("Get all Product URL :- " + getAllProductsUrl);

        productRequestBody = objectMapper.writeValueAsString(getAllProductsNode.path("body"));
        System.out.println("Product Body:- " + productRequestBody);

        Response getAllProductsResponse = APICommonMethod.sendRequest("POST", getAllProductsUrl, headers, productRequestBody);
        System.out.println("Get All Products Response Body: " + getAllProductsResponse.getBody().asString());

        Assert.assertEquals(getAllProductsResponse.getStatusCode(), getAllProductsNode.path("response").path("statusCode").asInt(), "GetAllProducts API failed!");
    }

    @When("I request product details")
    public void iRequestProductDetails() {
        productDetailsNode = JsonFileReader.getNode("Productdeatils");
        productDetailsUrl = productDetailsNode.path("url").asText();
        Response productDetailsResponse = APICommonMethod.sendRequest("GET", productDetailsUrl, headers, requestBody);
        System.out.println("Product Details Response Body: " + productDetailsResponse.getBody().asString());
        int actualStatusCode = productDetailsResponse.getStatusCode();
        int expectedStatusCode = productDetailsNode.path("response").path("statusCode").asInt();
        Assert.assertEquals(actualStatusCode, expectedStatusCode, "Status Code mismatch in ProductDetailsAPI!");
    }

    @When("I add a product to the cart")
    public void iAddAProductToTheCart() throws IOException {
        addToCartNode = JsonFileReader.getNode("addToCart");
        addToCartUrl = addToCartNode.path("url").asText();
        System.out.println("Add To Cart URL: " + addToCartUrl);

        addToCartRequestBody = objectMapper.writeValueAsString(addToCartNode.path("body"));
        System.out.println("Add to Cart Request Body: " + addToCartRequestBody);

        Response addToCartResponse = APICommonMethod.sendRequest("POST", addToCartUrl, headers, addToCartRequestBody);
        int expectedStatusCode = addToCartNode.path("response").path("statusCode").asInt();
        Assert.assertEquals(addToCartResponse.getStatusCode(), expectedStatusCode, "Status Code mismatch in AddToCartAPI!");
    }

    @When("I proceed to checkout")
    public void iProceedToCheckout() throws IOException {
        checkoutNode = JsonFileReader.getNode("checkout");
        checkoutUrl = checkoutNode.path("url").asText();
        System.out.println("Checkout URL: " + checkoutUrl);

        checkoutRequestBody = objectMapper.writeValueAsString(checkoutNode.path("body"));
        System.out.println("Add to Cart Request Body: " + checkoutRequestBody);

        Response CheckoutResponse = APICommonMethod.sendRequest("POST", checkoutUrl, headers, checkoutRequestBody);
        int expectedStatusCode = checkoutNode.path("response").path("statusCode").asInt();
        Assert.assertEquals(CheckoutResponse.getStatusCode(), expectedStatusCode, "Status Code mismatch in ChekoutAPI!");

    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();

        }
    }
}