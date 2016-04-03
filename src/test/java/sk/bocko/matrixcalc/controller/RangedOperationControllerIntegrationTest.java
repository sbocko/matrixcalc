package sk.bocko.matrixcalc.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.bocko.matrixcalc.Application;
import sk.bocko.matrixcalc.HttpGetWithEntity;
import static sk.bocko.matrixcalc.TestUtils.aRange;
import static sk.bocko.matrixcalc.TestUtils.anUnaryOperation;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:8888")
public class RangedOperationControllerIntegrationTest {

    private static final String URL = "http://localhost:8888/rest/";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testProcessReturnsErrorResponseWhenContentIsEmpty() throws IOException {
        // given
        String empty = "";

        // when
        CloseableHttpResponse actual = makeRequest(empty, anUnaryOperation(), aRange());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = String.format("{\"error\":\"Matrix is not present in request body.\"}", empty);
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testSumReturnsErrorResponseWhenMatrixIsMissing() throws IOException {
        // given
        String withoutMatrix = "{\"attribute\": \"value\"}";

        // when
        CloseableHttpResponse actual = makeRequest(withoutMatrix, anUnaryOperation(), aRange());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"error\":\"Matrix is not present in request body [{\\\"attribute\\\": \\\"value\\\"}].\"}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testSumReturnsErrorResponseWhenContentIsNotJson() throws IOException {
        // given
        String withoutMatrix = "not a json";

        // when
        CloseableHttpResponse actual = makeRequest(withoutMatrix, anUnaryOperation(), aRange());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = String.format("{\"error\":\"Request body is not a valid json: A JSONObject text must begin with '{' at 1 [character 2 line 1]\"}", withoutMatrix);
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testSumReturnsErrorResponseWhenMatrixHasInvalidType() throws IOException {
        // given
        String notAMatrix = "{\"matrix\": true}";

        // when
        CloseableHttpResponse actual = makeRequest(notAMatrix, anUnaryOperation(), aRange());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"error\":\"Request body is not a valid json: JSONObject[\\\"matrix\\\"] is not a JSONArray.\"}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testSumReturnsErrorResponseWhenMatrixIsEmpty() throws IOException {
        // given
        String notAMatrix = "{\"matrix\": []}";

        // when
        CloseableHttpResponse actual = makeRequest(notAMatrix, anUnaryOperation(), aRange());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = String.format("{\"error\":\"matrix is empty\"}", notAMatrix);
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testSumReturnsErrorResponseWhenMatrixIsNotRectangular() throws IOException {
        // given
        String matrix = "[[1.0, 2.0, 3.0],"
            + " [4.0, 5.0, 6.0, 7.0]," // 4 elements in 3x3 matrix
            + " [7.0, 8.0, 9.0]]";
        String notRectangular = "{\"matrix\": " + matrix + "}";

        // when
        CloseableHttpResponse actual = makeRequest(notRectangular, anUnaryOperation(), aRange());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = String.format("{\"error\":\"Matrix %s is not rectangular.\"}", matrix);
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testSumReturnsErrorResponseWhenMatrixIsNotNumeric() throws IOException {
        // given
        String matrix = "[[1, a],[3, 4]]";
        String notNumeric = "{\"matrix\": " + matrix + "}";

        // when
        CloseableHttpResponse actual = makeRequest(notNumeric, anUnaryOperation(), aRange());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"error\":\"Request body is not a valid json: JSONArray[1] is not a number.\"}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testProcessShouldHandleSummationCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2.1],[3.3,4]]}";
        final String argument = "1-x";
        final String operation = "sum";

        // when
        CloseableHttpResponse actual = makeRequest(body, operation, argument);

        //then
        assertThat(actual.getStatusLine().getStatusCode(), is(HttpStatus.OK.value()));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"result\":3.1}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testProcessShouldHandleProductCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2.1],[3.3,4]]}";
        final String argument = "2-x";
        final String operation = "product";

        // when
        CloseableHttpResponse actual = makeRequest(body, operation, argument);

        //then
        assertThat(actual.getStatusLine().getStatusCode(), is(HttpStatus.OK.value()));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"result\":13.2}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testProcessShouldHandleMinimumCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2.1],[3.3,4]]}";
        final String argument = "x-1";
        final String operation = "min";

        // when
        CloseableHttpResponse actual = makeRequest(body, operation, argument);

        //then
        assertThat(actual.getStatusLine().getStatusCode(), is(HttpStatus.OK.value()));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"result\":1}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testProcessShouldHandleMaximumCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2.1],[3.3,4]]}";
        final String argument = "x-2";
        final String operation = "max";

        // when
        CloseableHttpResponse actual = makeRequest(body, operation, argument);

        //then
        assertThat(actual.getStatusLine().getStatusCode(), is(HttpStatus.OK.value()));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"result\":4}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testProcessShouldHandleAverageCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2.1],[3.3,4]]}";
        final String argument = "x-2";
        final String operation = "average";

        // when
        CloseableHttpResponse actual = makeRequest(body, operation, argument);

        //then
        assertThat(actual.getStatusLine().getStatusCode(), is(HttpStatus.OK.value()));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"result\":3.05}";
        assertThat(actualContent, is(expectedContent));
    }

    private CloseableHttpResponse makeRequest(String content, String operation, String argument) {
        String params = "?range=" + argument;
        HttpRequestBase request = createRequest(content, operation, params);
        CloseableHttpClient client = HttpClientBuilder.create().build();

        try {
            return client.execute(request);
        } catch (IOException e) {
            String message = String.format("Exception thrown while calling " +
                "request with content %s and params %s", content, params);
            throw new IllegalStateException(message, e);
        }
    }

    private HttpRequestBase createRequest(String content, String operation, String urlParams) {
        HttpGetWithEntity request = new HttpGetWithEntity(URL + operation + urlParams);

        request.setHeader("Content-Type", "application/json");
        HttpEntity body = new StringEntity(content, Charset.forName("UTF-8"));
        request.setEntity(body);

        return request;
    }

}
