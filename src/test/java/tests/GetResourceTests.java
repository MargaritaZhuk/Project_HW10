package tests;

import io.restassured.response.Response;
import models.ResourceResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static specs.BaseRequestSpec.*;
import static specs.BaseResponseSpec.*;


@DisplayName("Тесты на получение данных")
public class GetResourceTests extends TestBase {

    private static final String BASE_PATH = "/api/unknown/";
    private static final int ID = 2;
    private static final int WRONG_ID = 23;

    @Test
    @DisplayName("Успешное получение данных пользователя")
    public void successResourceTest() {
        ResourceResponseModel response = step("Отправляем запрос на получение данных", () ->
                baseRequestSpec(BASE_PATH + ID)
                        .header("x-api-key", API_KEY)
                        .when()
                        .get()
                        .then()
                        .spec(baseResponseSpec(200))
                        .extract().as(ResourceResponseModel.class)
        );

        step("Проверяем ответ", () -> {
            assertNotNull(response.getData(), "Data не должна быть null");
            assertEquals(2, response.getData().getId(), "ID должен быть 2");
        });
    }

    @Test
    @DisplayName("Попытка получение данных несуществующего")
    public void notFoundResourceTest() {
        Response response = step("Отправляем запрос на получение данных", () ->
                baseRequestSpec(BASE_PATH + WRONG_ID)
                        .header("x-api-key", API_KEY)
                        .when()
                        .get()
        );

        step("Проверяем, что тело пустой объект {}", () -> {
                    response.then().spec(baseResponseSpec(404));
                    assertEquals("{}", response.getBody().asString());
                }
        );
    }

    @Test
    @DisplayName("Попытка получения данных без токена")
    public void forbiddenResourceTest() {
        Response response = step("Отправляем запрос на получение данных", () ->
                baseRequestSpec(BASE_PATH + ID)
                        .when()
                        .get()
        );

        step("Проверяем, что доступ запрещен", () ->
                response.then()
                .spec(baseResponseSpec(403))
        );
    }
}
