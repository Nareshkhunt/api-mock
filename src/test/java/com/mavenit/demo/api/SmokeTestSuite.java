package com.mavenit.demo.api;

import com.google.gson.JsonObject;
import com.mavenit.demo.api.services.ServiceHelper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.rootPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;


public class SmokeTestSuite {

    private ServiceHelper serviceHelper = new ServiceHelper();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "http://localhost:9999";
    }


    @Test
    public void getUserTest() {
        Response response = serviceHelper.exeuteGet("/register");
        assertThat("expecting 200 but status was something. ",
                201, is(equalTo(response.getStatusCode())));
    }

    @Test
    public void createUser() {
        String user = "automation-api-user";
        JsonObject payload = new JsonObject();
        payload.addProperty("password", "amar");
        payload.addProperty("passwordConfirmation", "amar");
        payload.addProperty("username", user);

        Response response = serviceHelper.exeutepostWithOutToken("/register", payload);

        //rest assured multiple validation
        response.then()
                .body("name", is(user))
                .body("message", is(equalTo("successful"))).statusCode(200);


        //rest assured single validation
        String name = response.then().extract().path("name");
        assertThat(name, is(equalTo(user)));

    }


    @Test
    public void postAblogTest() {
        String token = serviceHelper.generateToken();

        System.out.println("token: " + token);

        JsonObject payload = new JsonObject();
        payload.addProperty("body", "this is really useful for QA community, as this is really impr ");
        payload.addProperty("title", "api");


        Response response = serviceHelper.exeutepost("/post", payload, token);

        int statuscode = response.getStatusCode();
        if (statuscode == 401) {
            throw new RuntimeException("Hey invalid token generated.... please generate new one");
        }

        response.then()
                .body("message", is(equalToIgnoringCase("Post was published")))
                .statusCode(200);


    }


    @Test
    public void searchHotelPriceTest() {
        Response response = serviceHelper.exeuteGet("/search/hotels/availabilities");


        List<String> priceList = response.then().extract().path("hotelAvailabilities.ratePlans.rooms.totalCost.amount");

        int expectedHotels = response.then().extract().path("pageSize");


        assertThat(40,is(equalTo(priceList.size())));

    }

}
