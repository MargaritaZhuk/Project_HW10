package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static specs.BaseRequestSpec.baseRequestSpec;
import static specs.BaseResponseSpec.baseResponseSpec;

@DisplayName("Тесты на удаление")
public class DeleteUserTests extends TestBase {

    private static final String BASE_PATH = "/api/users/";


    @Test
    @DisplayName("Успешное удаление пользователя")
    public void successDeleteTest() {
        step("Отправляем запрос на удаление", () ->
                baseRequestSpec(BASE_PATH + 2)
                        .header("x-api-key", API_KEY)
                        .when()
                        .delete()
                        .then()
                        .spec(baseResponseSpec(204))
        );
    }
}
