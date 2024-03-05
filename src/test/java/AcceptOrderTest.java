import data_for_test.DataForCreationOrder;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AcceptOrderTest extends StepMethods{

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Successful accept order")
    public void successfulAcceptOrder() {
        DataForCreationOrder orderForData = new DataForCreationOrder("Тест", "Тест", "Тестовая, 12", "44", "79524657777", 2, "2024-03-08", "тест", new String[] {"","GREY"});

        sendPostRequestCreationCourier(jsonCourierData);
        Response loginResponse = sendPostRequestForCouriersLogin(jsonCourierLoginData);
        String courierId = getCourierId(loginResponse);

        Response orderCreationResponse = sendRequestOrderCreation(orderForData);
        String orderTrack = getOrderTrack(orderCreationResponse);
        Response responseOrder = sendRequestForOrderByTrack(orderTrack);
        String orderId = getOrderId(responseOrder);

        Response responseAcceptOrder =
                given()
                        .queryParam("courierId", courierId)
                        .when()
                        .put("/api/v1/orders/accept/" + orderId);
        responseAcceptOrder.then().assertThat().body("ok", equalTo(true)).statusCode(200);
    }

    @Test
    @DisplayName("Accepting an order without courier id")
    public void acceptingOrderWithoutCourierId() {
        DataForCreationOrder orderForData = new DataForCreationOrder("Мария", "Андреева", "Ленина, 4", "22", "79524568889", 5, "2024-03-13", "", new String[] {"",""});

        Response orderCreationResponse = sendRequestOrderCreation(orderForData);
        String orderTrack = getOrderTrack(orderCreationResponse);
        Response responseOrder = sendRequestForOrderByTrack(orderTrack);
        String orderId = getOrderId(responseOrder);


        Response responseAcceptOrder =
                given()
                        .queryParam("courierId", "")
                        .when()
                        .put("/api/v1/orders/accept/" + orderId);
        responseAcceptOrder.then().assertThat().body("message", equalTo("Недостаточно данных для поиска")).statusCode(400);
    }

    @Test
    @DisplayName("Accepting an order without order id")
    public void acceptingOrderWithoutOrderId() {
        String orderId = "";
        
        sendPostRequestCreationCourier(jsonCourierData);
        Response loginResponse = sendPostRequestForCouriersLogin(jsonCourierLoginData);
        String courierId = getCourierId(loginResponse);

        Response responseAcceptOrder =
                given()
                        .queryParam("courierId", courierId)
                        .when()
                        .put("/api/v1/orders/accept/" + orderId);
        //тест падает, т.к. ориентировалась на данные сваггера, тут по сути ручка та же,только нет айди заказа
        responseAcceptOrder.then().assertThat().body("message", equalTo("Недостаточно данных для поиска")).statusCode(400);
    }

    @Test
    @DisplayName("Accepting an order with incorrect courier id")
    public void acceptingOrderWithIncorrectCourierId() {
        DataForCreationOrder orderForData = new DataForCreationOrder("Тест", "Тест", "Тестовая, 12", "44", "79524657777", 2, "2024-03-08", "тест", new String[] {"","GREY"});

        sendPostRequestCreationCourier(jsonCourierData);

        Response orderCreationResponse = sendRequestOrderCreation(orderForData);
        String orderTrack = getOrderTrack(orderCreationResponse);
        Response responseOrder = sendRequestForOrderByTrack(orderTrack);
        String orderId = getOrderId(responseOrder);


        Response responseAcceptOrder =
                given()
                        .queryParam("courierId", "55667788")
                        .when()
                        .put("/api/v1/orders/accept/" + orderId);
        responseAcceptOrder.then().assertThat().body("message", equalTo("Курьера с таким id не существует")).statusCode(404);
    }

    @Test
    @DisplayName("Accepting an order with incorrect order id")
    public void acceptingOrderWithIncorrectOrderId() {
        DataForCreationOrder orderForData = new DataForCreationOrder("Александра", "Романова", "Парковая, 120", "16", "79524659933", 1, "2024-03-15", "какой-то коммент", new String[] {"BLACK","GREY"});
        String orderId = "98574321";

        sendPostRequestCreationCourier(jsonCourierData);
        Response loginResponse = sendPostRequestForCouriersLogin(jsonCourierLoginData);
        String courierId = getCourierId(loginResponse);

        sendRequestOrderCreation(orderForData);

        Response responseAcceptOrder =
                given()
                        .queryParam("courierId", courierId)
                        .when()
                        .put("/api/v1/orders/accept/" + orderId);
        responseAcceptOrder.then().assertThat().body("message", equalTo("Заказа с таким id не существует")).statusCode(404);
    }

    @After
    public void afterTest() {
        sendRequestDeleteCourier(sendPostRequestForCouriersLogin(jsonCourierLoginData));
        // удаление/отмена созданного заказа не добавлены во всех классах потому что, ручка "Отменить заказ" не работает совсем,
        // как не крутила и тут, и в постмане - все равно не работает. Отдает 400 код ошибки
        // а отдельной ручки на удаление нет.
    }
}
