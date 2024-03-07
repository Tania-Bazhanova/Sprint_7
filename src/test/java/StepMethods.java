import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import datafortest.DataForCancelOrder;
import datafortest.DataForCreationCourier;
import datafortest.DataForCreationOrder;
import datafortest.DataForLoginCourier;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StepMethods {
    protected final String login = "courierZachTan5";
    protected final String password = "54321";
    protected final String firstName = "courierZachTan5";

    protected final DataForLoginCourier jsonCourierLoginData = new DataForLoginCourier(login, password);
    protected final DataForCreationCourier jsonCourierData = new DataForCreationCourier(login, password, firstName);

    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCreationCourier(DataForCreationCourier jsonCourierData) {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(jsonCourierData)
                        .when()
                        .post("/api/v1/courier");

        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestForCouriersLogin(DataForLoginCourier jsonCourierLoginData) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(jsonCourierLoginData)
                        .when()
                        .post("/api/v1/courier/login");

        return response;
    }

    @Step("Send DELETE request to /api/v1/courier/:id")
    public Response sendRequestDeleteCourier(Response loginResponse) {
        Gson gson = new Gson();
        JsonObject object = gson.fromJson(loginResponse.body().asString(), JsonObject.class);
        JsonElement idElement = object.get("id");
        if (idElement != null) {
            return given().delete("/api/v1/courier/" + idElement.getAsString());
        }
        return null;
    }

    @Step("Send POST request to /api/v1/orders")
    public Response sendRequestOrderCreation(DataForCreationOrder dataForCreationOrder) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(dataForCreationOrder)
                        .when()
                        .post("/api/v1/orders");
        return response;
    }

    @Step("Send GET request to /api/v1/orders")
    public Response sendRequestGetListOrders() {
        Response response = given().get("/api/v1/orders");
        return response;
    }

    @Step("Get courier id")
    public String getCourierId(Response loginResponse) {
        Gson gson = new Gson();
        JsonObject objectLoginResponse = gson.fromJson(loginResponse.body().asString(), JsonObject.class);
        JsonElement courierId = objectLoginResponse.get("id");

        return courierId.getAsString();
    }

    @Step("Get order id")
    public String getOrderTrack(Response orderCreationResponse) {
        Gson gson = new Gson();
        JsonObject objectLoginResponse = gson.fromJson(orderCreationResponse.body().asString(), JsonObject.class);
        JsonElement orderTrack = objectLoginResponse.get("track");

        return orderTrack.getAsString();
    }

    @Step("Send get request for order by track")
    public Response sendRequestForOrderByTrack(String orderTrack) {
        Response responseForOrderByTrack =
                given()
                        .queryParam("t", orderTrack)
                        .get("api/v1/orders/track");
        return responseForOrderByTrack;
    }

    @Step("Get order id")
    public String getOrderId(Response responseForOrderByTrack) {
        Gson gson = new Gson();
        JsonObject objectLoginResponse = gson.fromJson(responseForOrderByTrack.body().asString(), JsonObject.class);
        String orderId = objectLoginResponse.getAsJsonObject("order").get("id").getAsString();

        return orderId;
    }

    @Step("Send GET request to /api/v1/orders/track")
    public Response sendGetRequestForOrderByTrack(String orderTrack) {
        return given()
                .queryParam("t", orderTrack)
                .when()
                .get("/api/v1/orders/track");
    }

    @Step("Send PUT request to /api/v1/orders/accept/")
    public Response sendPutRequestToAcceptOrder(String courierId, String orderId) {
        return given()
                .queryParam("courierId", courierId)
                .when()
                .put("/api/v1/orders/accept/" + orderId);
    }

    @Step("Send PUT request to /api/v1/orders/cancel")
    public Response sendPutRequestToCancelOrder(String track) {
        DataForCancelOrder jsonOrderData = new DataForCancelOrder(track);
        return given()
                .header("Content-type", "application/json")
                .body(jsonOrderData)
                .when()
                .put("/api/v1/orders/cancel");
    }

}
