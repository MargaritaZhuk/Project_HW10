package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@DisplayName("Тесты на получение данных")
public class GetResourceTests extends TestBase {
    @Test
    @DisplayName("Успещное получение данных пользователя")
    public void successResourceTest() {
        given().header("x-api-key", API_KEY).when().get("/unknown/2").then().log().ifValidationFails().statusCode(200).body("data", notNullValue()).body("data.id", is(2));
    }

    @Test
    @DisplayName("Попытка получение данных несуществующего")
    public void notFoundResourceTest() {
        given().header("x-api-key", API_KEY).when().get("/unknown/23").then().log().ifValidationFails().statusCode(404).body(is("{}"));
    }

    @Test
    @DisplayName("Попытка получения данных без токена")
    public void forbiddenResourceTest() {
        given().when().get("/unknown/23").then().log().ifValidationFails().statusCode(403);
    }
}
