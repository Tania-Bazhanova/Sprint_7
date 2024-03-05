import data_for_test.DataForCreationOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderByTrackTest extends StepMethods {
    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Get order by track")
    public void getOrderByTrack() {
        DataForCreationOrder orderForData = new DataForCreationOrder("Тест", "Тест", "Тестовая, 12", "44", "79524657777", 2, "2024-03-08", "тест", new String[]{"", "GREY"});

        Response orderCreationResponse = sendRequestOrderCreation(orderForData);
        String orderTrack = getOrderTrack(orderCreationResponse);

        Response responseOrderByTrack =
                given()
                        .queryParam("t", orderTrack)
                        .when()
                        .get("/api/v1/orders/track");
        responseOrderByTrack.then().assertThat().body("order.id", notNullValue()).statusCode(200);
    }

    @Test
    @DisplayName("Get order without track")
    public void getOrderWithoutTrack() {
        DataForCreationOrder orderForData = new DataForCreationOrder("Тест", "Тест", "Тестовая, 12", "44", "79524657777", 2, "2024-03-08", "тест", new String[]{"", "GREY"});

        sendRequestOrderCreation(orderForData);

        Response responseOrderWithoutTrack =
                given()
                        .queryParam("t", "")
                        .when()
                        .get("/api/v1/orders/track");
        responseOrderWithoutTrack.then().assertThat().body("message", equalTo("Недостаточно данных для поиска")).statusCode(400);
    }

    @Test
    @DisplayName("Get order with incorrect track")
    public void getOrderWithIncorrectTrack() {
        DataForCreationOrder orderForData = new DataForCreationOrder("Тест", "Тест", "Тестовая, 12", "44", "79524657777", 2, "2024-03-08", "тест", new String[]{"", "GREY"});

        sendRequestOrderCreation(orderForData);

        Response responseOrderWithIncorrectTrack =
                given()
                        .queryParam("t", "11889944")
                        .when()
                        .get("/api/v1/orders/track");
        responseOrderWithIncorrectTrack.then().assertThat().body("message", equalTo("Заказ не найден")).statusCode(404);
    }
}
