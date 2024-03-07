import datafortest.DataForCreationOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateOrderTest extends StepMethods {
    private final String[] selectedColor;
    private final int expectedStatusCode;
    String orderTrack = "";

    public CreateOrderTest(String[] selectedColor, int expectedStatusCode) {
        this.selectedColor = selectedColor;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters
    public static Object[][] getScooterColor() {
        return new Object[][] {
                {new String[]{"BLACK", "GREY"}, 201},
                {new String[]{"", "GREY"}, 201},
                {new String[]{"BLACK",""}, 201},
                {new String[]{"", ""}, 201}
        };
    }

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Create Order")
    public void creationOrder() {
        DataForCreationOrder testsData = new DataForCreationOrder("Тест", "Тест", "Тестовая, 12", "44", "79524657777", 2, "2024-03-08", "тест", selectedColor);
        Response response = sendRequestOrderCreation(testsData);
        orderTrack = getOrderTrack(response);
        response.then().assertThat().body("track", notNullValue()).statusCode(expectedStatusCode);
    }

    @After
    public void afterTest() {
        sendPutRequestToCancelOrder(orderTrack);
    }
}
