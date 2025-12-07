package tests;

import io.restassured.response.Response;
import models.RegistrationBodyModel;
import models.RegistrationResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.RegistrationSpec.*;

@DisplayName("Тесты на регистрацию")
public class RegistrationTests extends TestBase {

    @Test
    @DisplayName("Успешная регистрация - проверяем соответствие токена")
    public void successRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        RegistrationResponseModel response = step("Отправляем запрос на регистрацию", ()->
        given(registrationRequestSpec)
                .header("x-api-key", API_KEY)
                .body(registrationData)
                .when()
                .post()
                .then()
                .spec(registrationResponseSpec)
                .extract().as(RegistrationResponseModel.class));

        step("Проверяем валидность токена", () -> {
            assertNotNull(response.getToken(), "Token должен быть не null");
            assertEquals(17, response.getToken().length(), "Token должен содержать 17 символов");
            assertTrue(response.getToken().matches("[A-Za-z0-9]+"), "Token должен содержать только буквы и цифры");
        });
    }


    static Stream<Arguments> invalidRegistrationData() {
        return Stream.of(
                Arguments.of("Без email",
                        new RegistrationBodyModel(null, "pistol"), "Missing email or username"),
                Arguments.of("Без пароля",
                        new RegistrationBodyModel("test@test.com", null), "Missing password"),
                Arguments.of("Email - пустая строка",
                        new RegistrationBodyModel("", "pistol"), "Missing email or username"),
                Arguments.of("Невалидный email",
                        new RegistrationBodyModel("aa@", "pistol"), "Note: Only defined users succeed registration")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidRegistrationData")
    @DisplayName("Некорректные регистрационные данные")
    public void invalidRegistrationTest(String testName, RegistrationBodyModel registrationData, String expectedError) {

        Response response = step("Отправляем некорректный запрос {0}", () ->
                given(registrationRequestSpec)
                        .header("x-api-key", API_KEY)
                        .body(registrationData)
                        .when()
                        .post()
        );

        step("Проверяем ошибку в ответе", () ->
                response
                        .then()
                        .spec(errorResponseSpec(expectedError, null))
        );
    }

    @Test
    @DisplayName("Попытка регистрации с пустым объектом в body")
    public void registrationNoBodyTest() {
        String requestBody = "{}";
        Response response = step("Отправляем запрос на регистрацию", ()->
                given(registrationRequestSpec)
                        .header("x-api-key", API_KEY)
                        .body(requestBody)
                        .when()
                        .post());

        step("Проверяем текст ошибки", () -> {
            response.then()
                    .spec(errorResponseSpec("Missing email or username", null));
        });
    }

    @Test
    @DisplayName("Попытка регистрации с пустой строкой в body")
    public void registrationNullBodyTest() {
        String requestBody = "";
        Response response = step("Отправляем запрос на регистрацию", ()->
                given(registrationRequestSpec)
                        .header("x-api-key", API_KEY)
                        .body(requestBody)
                        .when()
                        .post());

        step("Проверяем текст ошибки", () -> {
            response.then()
                    .spec(errorResponseSpec("Empty request body", "Request body cannot be empty for JSON endpoints"));
        });
    }

    @Test
    @DisplayName("Попытка регистрации без токена")
    public void registrationForbiddenTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        Response response = step("Отправляем запрос на регистрацию", () ->
                given(registrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post());

        step("Проверяем, что доступ запрещен", () -> {
            response.then()
                    .statusCode(403);
        });
    }
}

