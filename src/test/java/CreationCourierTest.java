import data_for_test.DataForCreationCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreationCourierTest extends StepMethods {

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Create Courier")
    public void successfulCreationOfCourier() {
        Response creationResponse = sendPostRequestCreationCourier(jsonCourierData);
        creationResponse.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @Test
    @DisplayName("Create Courier with same data")
    public void creationCourierTwice() {
        sendPostRequestCreationCourier(jsonCourierData);
        Response creationResponse = sendPostRequestCreationCourier(jsonCourierData);
        // ориентировалась на текст ошибки в сваггере, с сервера приходит другой,поэтому тест падает
        creationResponse.then().assertThat().body("message", equalTo("Этот логин уже используется")).statusCode(409);
    }

    @Test
    @DisplayName("Create Courier without login")
    public void creationCourierWithOutLogin() {
        DataForCreationCourier jsonCourierData = new DataForCreationCourier("", password, firstName);

        Response response = sendPostRequestCreationCourier(jsonCourierData);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).statusCode(400);

    }

    @Test
    @DisplayName("Create Courier without password")
    public void creationCourierWithOutPassword() {
        DataForCreationCourier jsonCourierData = new DataForCreationCourier(login, "", firstName);

        Response response = sendPostRequestCreationCourier(jsonCourierData);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).statusCode(400);
    }

    @Test
    @DisplayName("Create Courier without firstname")
    public void creationCourierWithOutFirstName() {
        DataForCreationCourier jsonCourierData = new DataForCreationCourier(login, password, "");

        Response response = sendPostRequestCreationCourier(jsonCourierData);
        response.then().assertThat().body("ok", equalTo(true))
                .and()
                .statusCode(201);
    }

    @After
    public void afterTest() {
        sendRequestDeleteCourier(sendPostRequestForCouriersLogin(jsonCourierLoginData));
    }

}
