package tests;

import io.restassured.response.Response;
import models.ErrorResponseModel;
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
import static specs.BaseRequestSpec.baseRequestSpec;
import static specs.BaseResponseSpec.baseResponseSpec;

@DisplayName("Тесты на регистрацию")
public class RegistrationTests extends TestBase {

    private static final String BASE_PATH = "api/register";

    @Test
    @DisplayName("Успешная регистрация - проверяем соответствие токена")
    public void successRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        RegistrationResponseModel response = step("Отправляем запрос на регистрацию", ()->
        given(baseRequestSpec(BASE_PATH))
                .header("x-api-key", API_KEY)
                .body(registrationData)
                .when()
                .post()
                .then()
                .spec(baseResponseSpec(200))
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
                        new RegistrationBodyModel(null, "pistol"),
                        "Missing email or username",
                        null),
                Arguments.of("Без пароля",
                        new RegistrationBodyModel("test@test.com", null),
                        "Missing password",
                        null),
                Arguments.of("Email - пустая строка",
                        new RegistrationBodyModel("", "pistol"),
                        "Missing email or username",
                        null),
                Arguments.of("Невалидный email",
                        new RegistrationBodyModel("aa@", "pistol"),
                        "Note: Only defined users succeed registration",
                        null),
                Arguments.of("Пустая строка",
                        "",
                        "Empty request body",
                        "Request body cannot be empty for JSON endpoints"),
                Arguments.of("Строка с пробелом",
                        " ",
                        "Bad Request",
                        "Invalid request format"),
                Arguments.of("Пустой объект",
                        "{}",
                        "Missing email or username", null)
        );
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidRegistrationData")
    @DisplayName("Некорректные регистрационные данные")
    public void invalidRegistrationTest(String testName,
                                        Object requestBody,
                                        String expectedError) {

        Response response = step("Отправляем некорректный запрос " + testName, () ->
                given(baseRequestSpec(BASE_PATH))
                        .header("x-api-key", API_KEY)
                        .body(requestBody)
                        .when()
                        .post()
        );

        step("Проверяем статус и тело ошибки", () -> {
            response.then().spec(baseResponseSpec(400));

            ErrorResponseModel error = response.as(ErrorResponseModel.class);
            assertEquals(expectedError, error.getError());
        });
    }

    @Test
    @DisplayName("Попытка регистрации без токена")
    public void registrationForbiddenTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel();
        registrationData.setEmail("eve.holt@reqres.in");
        registrationData.setPassword("pistol");

        Response response = step("Отправляем запрос на регистрацию", () ->
                given(baseRequestSpec(BASE_PATH))
                        .body(registrationData)
                        .when()
                        .post());

        step("Проверяем, что доступ запрещен", () -> {
            response.then()
                    .spec(baseResponseSpec(403));
        });
    }
}

