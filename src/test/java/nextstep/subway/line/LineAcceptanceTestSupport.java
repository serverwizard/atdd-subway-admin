package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import org.springframework.http.MediaType;

public class LineAcceptanceTestSupport {

    public static ExtractableResponse<Response> createLine(LineRequest lineRequest) {
        return RestAssured
                .given()
                     .body(lineRequest)
                .when()
                     .contentType(MediaType.APPLICATION_JSON_VALUE)
                     .post("/lines")
                .then()
                .extract();
    }

    public static <T> T extractResult(ExtractableResponse<Response> response, Class<T> clazz) {
        return response.as(clazz);
    }

    public static ExtractableResponse<Response> findAllLines() {
        return RestAssured
                .given()
                .when()
                    .get("/lines")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> findLineById(Long id) {
        return RestAssured
                .given()
                .when()
                    .get("/lines/" + id)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> updateLine(Long id, LineRequest lineRequest) {
        return RestAssured
                .given()
                    .body(lineRequest)
                .when()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .put("/lines/" + id)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssured
                .given()
                .when()
                    .delete("/lines/" + id)
                .then()
                .extract();
    }
}
