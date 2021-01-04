package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static nextstep.subway.line.LineAcceptanceTestSupport.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        // given
        LineRequest lineRequest = LineRequest.of("2호선", "green");

        // when
        ExtractableResponse<Response> extractableResponse = LineAcceptanceTestSupport.createLine(lineRequest);
        LineResponse result = extractResult(extractableResponse, LineResponse.class);

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(result.getName()).isEqualTo(lineRequest.getName());
        assertThat(result.getColor()).isEqualTo(lineRequest.getColor());
    }

    @DisplayName("기존에 존재하는 지하철 노선 이름으로 지하철 노선을 생성한다.")
    @Test
    void createLine2() {
        // given
        LineRequest lineRequest = LineRequest.of("2호선", "green");
        LineAcceptanceTestSupport.createLine(lineRequest);

        // when, then
        assertThat(LineAcceptanceTestSupport.createLine(lineRequest).statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        LineResponse createdLine1 = extractResult(LineAcceptanceTestSupport.createLine(LineRequest.of("2호선", "green")), LineResponse.class);
        LineResponse createdLine2 = extractResult(LineAcceptanceTestSupport.createLine(LineRequest.of("7호선", "black")), LineResponse.class);

        // when
        ExtractableResponse<Response> extractableResponse = findAllLines();
        List<LineResponse> result = Arrays.asList(extractResult(extractableResponse, LineResponse[].class));

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(result).contains(createdLine1, createdLine2);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        LineResponse createdLine = extractResult(LineAcceptanceTestSupport.createLine(LineRequest.of("2호선", "green")), LineResponse.class);

        // when
        LineResponse result = extractResult(findLineById(createdLine.getId()), LineResponse.class);

        // then
        assertThat(result).isEqualTo(createdLine);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        LineResponse createdLine = extractResult(LineAcceptanceTestSupport.createLine(LineRequest.of("2호선", "green")), LineResponse.class);

        // when
        LineResponse result = extractResult(LineAcceptanceTestSupport.updateLine(createdLine.getId(), LineRequest.of("7호선", "black")), LineResponse.class);

        // then
        assertThat(result.getName()).isEqualTo("7호선");
        assertThat(result.getColor()).isEqualTo("black");
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        LineResponse createdLine = extractResult(LineAcceptanceTestSupport.createLine(LineRequest.of("2호선", "green")), LineResponse.class);

        // when
        ExtractableResponse<Response> extractableResponse = LineAcceptanceTestSupport.deleteLine(createdLine.getId());

        // then
        assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
