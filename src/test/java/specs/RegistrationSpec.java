package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

    public class RegistrationSpec {
        public static RequestSpecification registrationRequestSpec = with()
                .filter(withCustomTemplates())
                .log().uri()
                .log().body()
                .log().headers()
                .contentType(JSON)
                .basePath("/api/register");

        public static ResponseSpecification registrationResponseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(STATUS)
                .log(BODY)
                .build();

        public static ResponseSpecification errorResponseSpec(String expectedError, String expectedMessage) {
            ResponseSpecBuilder builder = new ResponseSpecBuilder()
                    .expectStatusCode(400)
                    .log(STATUS)
                    .log(BODY)
                    .expectBody("error", equalTo(expectedError));

            if (expectedMessage != null) {
                builder.expectBody("message", containsString(expectedMessage));
            }

            return builder.build();
        }
    }
