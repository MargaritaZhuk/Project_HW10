package specs;

import io.restassured.specification.RequestSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;

public class BaseRequestSpec {
    public static RequestSpecification baseRequestSpec(String basePath) {
        return given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .contentType(JSON)
                .basePath(basePath);
    }
}
