package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static org.hamcrest.Matchers.*;

public class GetResourceSpec {

    public static RequestSpecification getResourceSpec(String id) {
        return given()
                .filter(withCustomTemplates())
                .log().uri()
                .log().headers()
                .basePath("/api/unknown/" + id);
    }

    public static ResponseSpecification getResourceResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(STATUS)
            .log(BODY)
            .build();

    public static ResponseSpecification errorResponseSpec(int statusCode, String expectedBody) {
        ResponseSpecBuilder builder = new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(STATUS)
                .log(BODY);

        if (expectedBody != null) {
            builder.expectBody(is(expectedBody));
        }
        return builder.build();
    }


}
