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

/**
 * Created by stefan on 3/23/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:8888")
public class AdditionControllerIntegrationTest {

    private static final String URL = "http://localhost:8888/rest/add/";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testAdditionReturnsErrorResponseWhenContentIsEmpty() throws IOException {
        // given
        String empty = "";

        // when
        CloseableHttpResponse actual = makeRequest(empty, urlParams());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = String.format("{\"error\":\"Matrix is not present in request body.\"}", empty);
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testAdditionReturnsErrorResponseWhenMatrixIsMissing() throws IOException {
        // given
        String withoutMatrix = "{\"attribute\": \"value\"}";

        // when
        CloseableHttpResponse actual = makeRequest(withoutMatrix, urlParams());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"error\":\"Matrix is not present in request body [{\\\"attribute\\\": \\\"value\\\"}].\"}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testAdditionReturnsErrorResponseWhenContentIsNotJson() throws IOException {
        // given
        String withoutMatrix = "not a json";

        // when
        CloseableHttpResponse actual = makeRequest(withoutMatrix, urlParams());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = String.format("{\"error\":\"Request body is not a valid json: A JSONObject text must begin with '{' at 1 [character 2 line 1]\"}", withoutMatrix);
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testAdditionReturnsErrorResponseWhenMatrixHasInvalidType() throws IOException {
        // given
        String notAMatrix = "{\"matrix\": true}";

        // when
        CloseableHttpResponse actual = makeRequest(notAMatrix, urlParams());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"error\":\"Request body is not a valid json: JSONObject[\\\"matrix\\\"] is not a JSONArray.\"}";
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testAdditionReturnsErrorResponseWhenMatrixIsEmpty() throws IOException {
        // given
        String notAMatrix = "{\"matrix\": []}";

        // when
        CloseableHttpResponse actual = makeRequest(notAMatrix, urlParams());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = String.format("{\"error\":\"matrix is empty\"}", notAMatrix);
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testAdditionReturnsErrorResponseWhenMatrixIsNotRectangular() throws IOException {
        // given
        String matrix = "[[1, 2, 3],"
            + " [4, 5, 6, 7]," // 4 elements in 3x3 matrix
            + " [7, 8, 9]]";
        String notRectangular = "{\"matrix\": " + matrix + "}";

        // when
        CloseableHttpResponse actual = makeRequest(notRectangular, urlParams());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = String.format("{\"error\":\"Matrix %s is not rectangular.\"}", matrix);
        assertThat(actualContent, is(expectedContent));
    }

    @Test
    public void testAdditionReturnsErrorResponseWhenMatrixIsNotNumeric() throws IOException {
        // given
        String matrix = "[[1, a],[3, 4]]";
        String notNumeric = "{\"matrix\": " + matrix + "}";

        // when
        CloseableHttpResponse actual = makeRequest(notNumeric, urlParams());

        // then
        int expectedStatusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
        int actualStatusCode = actual.getStatusLine().getStatusCode();
        assertThat(actualStatusCode, is(expectedStatusCode));

        String actualContent = IOUtils.toString(actual.getEntity().getContent());
        String expectedContent = "{\"error\":\"Request body is not a valid json: JSONArray[1] is not a number.\"}";
        assertThat(actualContent, is(expectedContent));
    }

    private String urlParams() {
        return "1/2";
    }

    private CloseableHttpResponse makeRequest(String content, String params) {
        HttpRequestBase request = createRequest(content, params);
        CloseableHttpClient client = HttpClientBuilder.create().build();

        try {
            return client.execute(request);
        } catch (IOException e) {
            String message = String.format("Exception thrown while calling " +
                "request with content %s and params %s", content, params);
            throw new IllegalStateException(message, e);
        }
    }

    private HttpRequestBase createRequest(String content, String urlParams) {
        HttpGetWithEntity request = new HttpGetWithEntity(URL + urlParams);

        request.setHeader("Content-Type", "application/json");
        HttpEntity body = new StringEntity(content, Charset.forName("UTF-8"));
        request.setEntity(body);

        return request;
    }

}