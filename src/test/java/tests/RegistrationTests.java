package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты на регистрацию")
public class RegistrationTests extends TestBase {

    private static final String REGISTRATION_PATH = "/register";

    @Test
    @DisplayName("Успешная регистрация - проверяем соответствие токена")
    public void successRegistrationTest() {
        String requestBody = """
                {
                    "email": "eve.holt@reqres.in",
                    "password": "pistol"
                }""";
        given()
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .contentType(JSON)
                .log().ifValidationFails()
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(200)
                .body("token", notNullValue())
                .body("token", allOf(
                        hasLength(17),
                        matchesPattern("[A-Za-z0-9]+")
                ));
    }

    @Test
    @DisplayName("Попытка регистрации без email")
    public void registrationWithoutEmailTest() {
        String requestBody = """
                {
                    "password": "pistol"
                }""";
        given()
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .contentType(JSON)
                .log().ifValidationFails()
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    @DisplayName("Попытка регистрации без пароля")
    public void registrationWithoutPasswordTest() {
        String requestBody = """
                {"
                "email: "eve.holt@reqres.in"
                }""";
        given()
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .contentType(JSON)
                .log()
                .ifValidationFails()
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(400)
                .body("error", is("Bad Request"))
                .body("message", is("Invalid request format"));
    }

    @Test
    @DisplayName("Попытка регистрации с пустым объектом в body")
    public void registrationNoBodyTest() {
        String requestBody = "{}";
        given()
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .contentType(JSON)
                .log().ifValidationFails()
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .log()
                .ifValidationFails()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    @DisplayName("Попытка регистрации с пустой строкой в body")
    public void registrationNullBodyTest() {
        String requestBody = "";
        given()
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .contentType(JSON)
                .log().ifValidationFails()
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(400)
                .body("error", is("Empty request body"))
                .body("message", is("Request body cannot be empty for JSON endpoints"));
    }

    @Test
    @DisplayName("Попытка регистрации с пустой почтой")
    public void registrationEmptyEmailTest() {
        String requestBody = """
                {
                    "email": "",
                    "password": "pistol"
                }""";
        given()
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .contentType(JSON)
                .log().ifValidationFails()
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }

    @Test
    @DisplayName("Попытка регистрации с невалидной почтой")
    public void registrationWrongEmailTest() {
        String requestBody = """
                {
                    "email": "aa@",
                    "password": "pistol"
                }""";
        given()
                .header("x-api-key", API_KEY).body(requestBody)
                .contentType(JSON)
                .log().ifValidationFails()
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(400)
                .body("error", is("Note: Only defined users succeed registration"));
    }

    @Test
    @DisplayName("Попытка регистрации без токена")
    public void registrationForbiddenTest() {
        String requestBody = """
                {
                    "email": "eve.holt@reqres.in",
                    "password": "pistol"
                }""";
        given()
                .body(requestBody)
                .contentType(JSON)
                .log().ifValidationFails()
                .when()
                .post(REGISTRATION_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(403);
    }
}

