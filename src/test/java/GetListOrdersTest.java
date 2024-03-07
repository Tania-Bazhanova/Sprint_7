import datafortest.DataForCreationOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetListOrdersTest extends StepMethods {
    String orderTrack = "";

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Get List Orders")
    public void getListOrders() {
        DataForCreationOrder ordersData = new DataForCreationOrder("Тест", "Автоматический", "Тестовая, 21", "17", "79657069574", 3, "2024-03-08", "тест", new String[]{"BLACK", ""});
        Response responseOrder = sendRequestOrderCreation(ordersData);
        orderTrack = getOrderTrack(responseOrder);


        Response responseListOrders = sendRequestGetListOrders();
        responseListOrders.then().assertThat().body("orders.track", notNullValue()).statusCode(200);
    }

    @After
    public void afterTest() {
        sendPutRequestToCancelOrder(orderTrack);
    }
}
