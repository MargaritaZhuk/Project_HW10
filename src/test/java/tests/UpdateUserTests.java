package tests;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Тесты на изменение данных")
public class UpdateUserTests extends TestBase {

    private static final Faker faker = new Faker();
    private static final String UPDATE_PATH = "/users/2";

    @Test
    @DisplayName("Успешное изменение данных пользователя")
    public void successUserUpdate() {
        String name = faker.name().fullName();
        String job = faker.job().position();

        Map<String, String> requestBody = Map.of("name", name, "job", job);

        given()
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .contentType(JSON)
                .when()
                .put(UPDATE_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("name", is(name))
                .body("job", is(job));
    }

    @Test
    @DisplayName("Обновление времени апдейта при изменении данных пользователя")
    public void successUserUpdateTimeTest() {
        String name = faker.name().fullName();
        String job = faker.job().position();

        Map<String, String> requestBody = Map.of("name", name, "job", job);

        OffsetDateTime beforeRequest = OffsetDateTime.now(ZoneOffset.UTC);

        Response response =
                given()
                        .header("x-api-key", API_KEY)
                        .body(requestBody)
                        .contentType(JSON)
                        .when()
                        .put(UPDATE_PATH)
                        .then()
                        .log().ifValidationFails()
                        .statusCode(200)
                        .extract().response();

        OffsetDateTime updatedAt = OffsetDateTime.parse(response.path("updatedAt"));

        assertTrue(updatedAt.isAfter(beforeRequest));
    }

    @Test
    @DisplayName("Пустое поле в body при изменении данных пользователя")
    public void emptyBodyUserUpdate() {
        String requestBody = "";
        given()
                .header("x-api-key", API_KEY)
                .body(requestBody)
                .contentType(JSON)
                .when()
                .put(UPDATE_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(400)
                .body("error", is("Empty request body"))
                .body("message", is("Request body cannot be empty for JSON endpoints"));
    }
}
