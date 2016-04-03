package sk.bocko.matrixcalc.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static sk.bocko.matrixcalc.TestUtils.aBody;
import static sk.bocko.matrixcalc.TestUtils.aRange;
import static sk.bocko.matrixcalc.TestUtils.anUnaryOperation;

public class RangedOperationRequestHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final RangedOperationRequestHandler testee = new RangedOperationRequestHandler();

    @Test
    public void testHandleShouldHandleSumCorrectly() {
        // given
        final String body = "{\"matrix\":[[1,2],[3.4,4]]}";
        final String range = "2-x";
        final String operation = "sum";

        // when
        JSONObject actual = testee.handle(body, range, operation);

        // then
        final JSONObject expected = new JSONObject().put("result", 7.4d);
        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testHandleShouldHandleProductCorrectly() {
        // given
        final String body = "{\"matrix\":[[1,2],[3.4,4]]}";
        final String range = "x-2";
        final String operation = "product";

        // when
        JSONObject actual = testee.handle(body, range, operation);

        // then
        final JSONObject expected = new JSONObject().put("result", 8d);
        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testHandleShouldHandleMinimumCorrectly() {
        // given
        final String body = "{\"matrix\":[[1,2],[3.4,4]]}";
        final String range = "x-1";
        final String operation = "min";

        // when
        JSONObject actual = testee.handle(body, range, operation);

        // then
        final JSONObject expected = new JSONObject().put("result", 1d);
        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testHandleShouldHandleMaximumCorrectly() {
        // given
        final String body = "{\"matrix\":[[1,2],[3.4,4]]}";
        final String range = "1-x";
        final String operation = "max";

        // when
        JSONObject actual = testee.handle(body, range, operation);

        // then
        final JSONObject expected = new JSONObject().put("result", 2d);
        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testHandleShouldThrowWhenInvalidMatrix() {
        // given
        final String invalid = "{\"matrix\":'invalid'}";

        // then
        thrown.expect(JSONException.class);
        thrown.expectMessage("not a JSONArray.");

        // when
        testee.handle(invalid, aRange(), anUnaryOperation());
    }

    @Test
    public void testHandleShouldThrowWhenNullMatrix() {
        // then
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("request body is null");

        // when
        testee.handle(null, aRange(), anUnaryOperation());
    }

    @Test
    public void testHandleShouldThrowWhenInvalidRange() {
        // given
        final String invalid = "not a range";

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid range", invalid));

        // when
        testee.handle(aBody(), invalid, anUnaryOperation());
    }

    @Test
    public void testHandleShouldThrowWhenInvalidOperation() {
        // given
        String invalid = "not an operation";

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("Unsupported operation '%s'", invalid));

        // when
        testee.handle(aBody(), anUnaryOperation(), invalid);
    }

    @Test
    public void testHandleShouldThrowWhenNullOperation() {
        // then
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("name of the operation is null");

        // when
        testee.handle(aBody(), anUnaryOperation(), null);
    }

}
