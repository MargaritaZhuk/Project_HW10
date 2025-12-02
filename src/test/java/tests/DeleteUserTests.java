package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@DisplayName("Тесты на удаление")
public class DeleteUserTests extends TestBase {
    @Test
    @DisplayName("Успешное удаление пользователя")
    public void successDeleteTest() {
        given().header("x-api-key", API_KEY).when().delete("/users/2").then().log().all().statusCode(204);
    }
}
