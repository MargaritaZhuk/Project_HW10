package tests;

import io.restassured.response.ValidatableResponse;
import models.ResourceResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.GetResourceSpec.*;


@DisplayName("Тесты на получение данных")
public class GetResourceTests extends TestBase {

    @Test
    @DisplayName("Успешное получение данных пользователя")
    public void successResourceTest() {
        ResourceResponseModel response = step("Отправляем запрос на получение данных", () ->
                getResourceSpec("2")
                        .header("x-api-key", API_KEY)
                        .when()
                        .get()
                        .then()
                        .spec(getResourceResponseSpec)
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
        ValidatableResponse response = step("Отправляем запрос на получение данных", () ->
                getResourceSpec("23")
                        .header("x-api-key", API_KEY)
                        .when()
                        .get()
                        .then()
        );

        step("Проверяем, что тело пустой объект {}", () ->
                response.spec(errorResponseSpec(404, "{}"))
        );
    }

    @Test
    @DisplayName("Попытка получения данных без токена")
    public void forbiddenResourceTest() {
        ValidatableResponse response = step("Отправляем запрос на получение данных", () ->
                getResourceSpec("23")
                        .when()
                        .get()
                        .then()
        );

        step("Проверяем, что доступ запрещен", () ->
                response.spec(errorResponseSpec(403, null))
        );
    }
}
