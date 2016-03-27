package sk.bocko.matrixcalc.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static sk.bocko.matrixcalc.TestUtils.aBody;
import static sk.bocko.matrixcalc.TestUtils.anOperandIndex;
import static sk.bocko.matrixcalc.TestUtils.anOperation;

public class BinaryOperationRequestHandlerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final BinaryOperationRequestHandler testee = new BinaryOperationRequestHandler();

    @Test
    public void testHandleShouldHandleAdditionCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2.1],[3.3,4]]}";
        final String firstOperandIndex = "1-2"; // 2.1
        final String secondOperandIndex = "2-1"; // 3.3
        final String operation = "add";

        // when
        JSONObject actual = testee.handle(body, firstOperandIndex, secondOperandIndex, operation);

        //then
        final JSONObject expected = new JSONObject().put("result", 5.4);
        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testHandleShouldHandleSubtractionCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2],[3.4,4]]}";
        final String firstOperandIndex = "1-2"; // 2
        final String secondOperandIndex = "2-1"; // 3.4
        final String operation = "subtract";

        // when
        JSONObject actual = testee.handle(body, firstOperandIndex, secondOperandIndex, operation);

        //then
        final JSONObject expected = new JSONObject().put("result", -1.4);
        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testHandleShouldHandleMultiplicationCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2],[3.4,4]]}";
        final String firstOperandIndex = "1-2"; // 2
        final String secondOperandIndex = "2-1"; // 3.4
        final String operation = "multiply";

        // when
        JSONObject actual = testee.handle(body, firstOperandIndex, secondOperandIndex, operation);

        //then
        final JSONObject expected = new JSONObject().put("result", 6.8);
        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testHandleShouldHandleDivisionCorrectly() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2],[3.4,4]]}";
        final String firstOperandIndex = "1-2"; // 2
        final String secondOperandIndex = "2-2"; // 4
        final String operation = "divide";

        // when
        JSONObject actual = testee.handle(body, firstOperandIndex, secondOperandIndex, operation);

        //then
        final JSONObject expected = new JSONObject().put("result", 0.5);
        assertThat(actual.toString(), is(expected.toString()));
    }

    @Test
    public void testHandleShouldThrowWhenInvalidMatrix() throws Exception {
        // given
        final String body = "{\"matrix\": 42";

        // then
        thrown.expect(JSONException.class);

        // when
        testee.handle(body, anOperandIndex(), anOperandIndex(), anOperation());
    }

    @Test
    public void testHandleShouldThrowWhenInvalidOperation() throws Exception {
        // given
        final String invalid = "invalidOperation";

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("Unsupported operation '%s'", invalid));

        // when
        testee.handle(aBody(), anOperandIndex(), anOperandIndex(), invalid);
    }

    @Test
    public void testHandleShouldThrowWhenResultIsInfinite() throws Exception {
        // given
        final String body = "{\"matrix\":[[1," + Double.MAX_VALUE + "]," +
            "[" + Double.MAX_VALUE + ",4]]}";
        final String firstOperandIndex = "1-2"; // Double.MAX_VALUE
        final String secondOperandIndex = "2-1"; // Double.MAX_VALUE
        final String operation = "add";

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("result is not a finite number");

        // when
        testee.handle(body, firstOperandIndex, secondOperandIndex, operation);
    }

    @Test
    public void testHandleShouldThrowWhenDivisionByZero() throws Exception {
        // given
        final String body = "{\"matrix\":[[1,2],[3.4,0]]}";
        final String firstOperandIndex = "1-2"; // 2
        final String zero = "2-2"; // 0
        final String operation = "divide";

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("result is not a finite number");

        // when
        testee.handle(body, firstOperandIndex, zero, operation);
    }
}
