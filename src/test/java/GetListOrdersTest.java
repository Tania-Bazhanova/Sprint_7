import data_for_test.DataForCreationOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;

public class GetListOrdersTest extends StepMethods {

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Get List Orders")
    public void getListOrders() {
        DataForCreationOrder ordersData = new DataForCreationOrder("Тест", "Автоматический", "Тестовая, 21", "17", "79657069574", 3, "2024-03-08", "тест", new String[] {"BLACK", ""});
        sendRequestOrderCreation(ordersData);

        Response responseListOrders = sendRequestGetListOrders();
        responseListOrders.then().assertThat().body("orders.track", notNullValue()).statusCode(200);
    }
}
