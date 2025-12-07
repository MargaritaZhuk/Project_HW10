package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static specs.BaseRequestSpec.baseRequestSpec;
import static specs.BaseResponseSpec.baseResponseSpec;

@DisplayName("Тесты на удаление")
public class DeleteUserTests extends TestBase {

    private static final String BASE_PATH = "/api/users/";
    private static final int ID = 2;

    @Test
    @DisplayName("Успешное удаление пользователя")
    public void successDeleteTest() {
        Response response = step("Отправляем запрос на удаление", () ->
                baseRequestSpec(BASE_PATH + ID)
                        .header("x-api-key", API_KEY)
                        .when()
                        .delete()
        );

        step("Проверяем статус-код", () ->
                response.then().spec(baseResponseSpec(204))
        );
    }
}
