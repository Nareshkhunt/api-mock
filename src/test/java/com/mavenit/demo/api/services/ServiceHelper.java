package com.mavenit.demo.api.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class ServiceHelper {


    public Response exeuteGet(String endPoint) {
        return given().contentType("application/json")
                .when()
                .get(endPoint);
    }


    public Response exeutepost(String endPoint, JsonObject payload, String token) {
        return given().contentType("application/json")
                .when().queryParam("access_token", token)
                .body(payload)
                .post(endPoint);
    }

    public Response exeutepostWithOutToken(String endPoint, JsonObject payload) {
        return given().contentType("application/json")
                .when()
                .body(payload)
                .post(endPoint);
    }

    public Response exeutepost(String endPoint, JsonArray payload) {
        return given().contentType("application/json")
                .when()
                .body(payload)
                .post(endPoint);
    }

    public Response exeutePut(String endPoint, JsonObject payload, int id) {
        return given().contentType("application/json")
                .when()
                .body(payload)
                .put(endPoint + id);
    }

    public Response exeuteDelete(String endPoint, int id) {
        return given().contentType("application/json")
                .when()
                .delete(endPoint + id);
    }

    public Response exeuteGet(String endPoint, int id) {
        return given().contentType("application/json")
                .when()
                .delete(endPoint + id);
    }

    public String generateToken() {
        Response response = given()
                .auth().basic("my-trusted-client", "secret")
                .when()
                .post("/oauth/token?grant_type=password&username=admin&password=admin");

        return response.then().extract().path("access_token").toString();
    }
}
