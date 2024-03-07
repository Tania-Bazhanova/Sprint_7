import datafortest.DataForLoginCourier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierInSystemTest extends StepMethods {

    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Login Courier")
    public void loginCourier() {
        sendPostRequestCreationCourier(jsonCourierData);
        Response loginResponse = sendPostRequestForCouriersLogin(jsonCourierLoginData);

        loginResponse.then().assertThat().body("id", notNullValue()).statusCode(200);
    }

    @Test
    @DisplayName("Login Courier without login")
    public void loginCourierWithOutLogin() {
        DataForLoginCourier jsonCourierLoginData = new DataForLoginCourier("", password);

        Response response = sendPostRequestForCouriersLogin(jsonCourierLoginData);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).statusCode(400);

    }

    @Test
    @DisplayName("Login Courier without password")
    public void loginCourierWithOutPassword() {
        DataForLoginCourier jsonCourierLoginData = new DataForLoginCourier(login, "");

        Response response = sendPostRequestForCouriersLogin(jsonCourierLoginData);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа")).statusCode(400);
    }

    @Test
    @DisplayName("Login Courier with incorrect login")
    public void loginCourierWithIncorrectLogin() {
        DataForLoginCourier dataWithIncorrectLogin = new DataForLoginCourier("hufjnfk", password);
        Response loginResponse = sendPostRequestForCouriersLogin(dataWithIncorrectLogin);

        loginResponse.then().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
    }

    @Test
    @DisplayName("Login Courier with incorrect password")
    public void loginCourierWithIncorrectPassword() {
        DataForLoginCourier dataWithIncorrectPassword = new DataForLoginCourier(login, "59522");

        sendPostRequestCreationCourier(jsonCourierData);
        Response loginResponse = sendPostRequestForCouriersLogin(dataWithIncorrectPassword);

        loginResponse.then().body("message", equalTo("Учетная запись не найдена")).statusCode(404);
    }

    @After
    public void afterTests() {
        sendRequestDeleteCourier(sendPostRequestForCouriersLogin(jsonCourierLoginData));
    }
}
