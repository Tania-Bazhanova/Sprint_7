import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest extends StepMethods {
    @Before
    public void setUpUrl() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Delete Courier")
    public void deleteCourier() {
        sendPostRequestCreationCourier(jsonCourierData);
        Response deleteResponse = sendRequestDeleteCourier(sendPostRequestForCouriersLogin(jsonCourierLoginData));
        deleteResponse.then().assertThat().body("ok", equalTo(true)).statusCode(200);
    }

    @Test
    @DisplayName("Delete Courier without id")
    public void deleteCourierWithoutId() {
        sendPostRequestCreationCourier(jsonCourierData);
        sendPostRequestForCouriersLogin(jsonCourierLoginData);
        Response deleteResponse = given().delete("/api/v1/courier/");
        deleteResponse.then().assertThat().body("message",  equalTo("Недостаточно данных для удаления курьера")).statusCode(400);
    }

    @Test
    @DisplayName("Delete Courier with incorrect id")
    public void deleteCourierWithIncorrectId() {
        sendPostRequestCreationCourier(jsonCourierData);
        sendPostRequestForCouriersLogin(jsonCourierLoginData);
        Response deleteResponse = given().delete("/api/v1/courier/551515151");
        //ориентировалась на сваггер когда писала проверку текста ошибки, в нем без . текст
        deleteResponse.then().assertThat().body("message",  equalTo("Курьера с таким id нет")).statusCode(404);
    }

}
