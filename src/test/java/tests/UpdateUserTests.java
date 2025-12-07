package tests;

import com.github.javafaker.Faker;
import models.UpdateBodyModel;
import models.ErrorResponseModel;
import models.UpdateResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.BaseRequestSpec.baseRequestSpec;
import static specs.BaseResponseSpec.baseResponseSpec;

@DisplayName("Тесты на изменение данных")
public class UpdateUserTests extends TestBase {

    private static final Faker faker = new Faker();
    private static final String BASE_PATH = "api/users/";

    @Test
    @DisplayName("Успешное изменение данных пользователя")
    public void successUserUpdate() {
        String name = faker.name().fullName();
        String job = faker.job().position();

        UpdateBodyModel updateData = new UpdateBodyModel();
        updateData.setName(name);
        updateData.setJob(job);

        UpdateResponseModel response = step("Отправляем запрос на изменнение данных", ()->
                given(baseRequestSpec(BASE_PATH + 2))
                        .header("x-api-key", API_KEY)
                        .body(updateData)
                        .when()
                        .put()
                        .then()
                        .spec(baseResponseSpec(200))
                        .extract().as(UpdateResponseModel.class));

        step("Проверяем, что данные изменены", () -> {
            assertEquals(name, response.getName());
            assertEquals(job, response.getJob());
        });
    }

    @Test
    @DisplayName("Обновление времени апдейта при изменении данных пользователя")
    public void successUserUpdateTimeTest() {
        String name = faker.name().fullName();
        String job = faker.job().position();

        UpdateBodyModel updateData = new UpdateBodyModel();
        updateData.setName(name);
        updateData.setJob(job);

        OffsetDateTime beforeRequest = OffsetDateTime.now(ZoneOffset.UTC);

        UpdateResponseModel response = step("Отправляем запрос на изменнение данных", ()->
                given(baseRequestSpec(BASE_PATH + 2))
                        .header("x-api-key", API_KEY)
                        .body(updateData)
                        .when()
                        .put()
                        .then()
                        .spec(baseResponseSpec(200))
                        .extract().as(UpdateResponseModel.class));

        step("Проверяем, что время изменения данных совпадает", () -> {
            OffsetDateTime updatedAt = OffsetDateTime.parse(response.getUpdatedAt());
            assertTrue(updatedAt.isAfter(beforeRequest));
        });
    }

    @Test
    @DisplayName("Пустое поле в body при изменении данных пользователя")
    public void emptyBodyUserUpdate() {
        String requestBody = "";
        ErrorResponseModel response = step("Отправляем запрос на изменнение данных", ()->
                given(baseRequestSpec(BASE_PATH + 2))
                        .header("x-api-key", API_KEY)
                        .body(requestBody)
                        .when()
                        .put()
                        .then()
                        .spec(baseResponseSpec(400))
                        .extract().as(ErrorResponseModel.class)
        );

        step("Проверяем ошибку в ответе", () -> {
            assertEquals("Empty request body", response.getError());
            assertEquals("Request body cannot be empty for JSON endpoints", response.getMessage());
        });
    }
}
