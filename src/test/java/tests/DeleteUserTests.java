package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static specs.DeleteUserSpec.deleteUserRequestSpec;
import static specs.DeleteUserSpec.deleteUserResponseSpec;

@DisplayName("Тесты на удаление")
public class DeleteUserTests extends TestBase {
    @Test
    @DisplayName("Успешное удаление пользователя")
    public void successDeleteTest() {
        step("Отправляем запрос на удаление", () ->
                deleteUserRequestSpec("2")
                        .header("x-api-key", API_KEY)
                        .when()
                        .delete()
                        .then()
                        .spec(deleteUserResponseSpec)
        );
    }
}
