package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    protected static final String API_KEY = "reqres_a37e4386c6fa47fbb39791e6a1d6f4bc";

    @BeforeAll
    static void setupApiEnvironment() {
        RestAssured.baseURI = "https://reqres.in/";
    }
}
