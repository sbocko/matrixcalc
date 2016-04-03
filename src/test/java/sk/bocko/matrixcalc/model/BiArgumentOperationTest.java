package sk.bocko.matrixcalc.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static sk.bocko.matrixcalc.model.BiArgumentOperation.ADDITION;
import static sk.bocko.matrixcalc.model.BiArgumentOperation.DIVISION;
import static sk.bocko.matrixcalc.model.BiArgumentOperation.MULTIPLICATION;
import static sk.bocko.matrixcalc.model.BiArgumentOperation.SUBTRACTION;
import static sk.bocko.matrixcalc.model.BiArgumentOperation.from;

public class BiArgumentOperationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testShouldCreateEnumFromStringForAddition() {
        // given
        String addition = "add";

        // when
        BiArgumentOperation actual = from(addition);

        //then
        assertThat(actual, is(ADDITION));
    }

    @Test
    public void testShouldCreateEnumFromStringForSubtraction() {
        // given
        String subtraction = "subtract";

        // when
        BiArgumentOperation actual = from(subtraction);

        //then
        assertThat(actual, is(SUBTRACTION));
    }

    @Test
    public void testShouldCreateEnumFromStringForMultiplication() {
        // given
        String multiplication = "multiply";

        // when
        BiArgumentOperation actual = from(multiplication);

        //then
        assertThat(actual, is(MULTIPLICATION));
    }

    @Test
    public void testShouldCreateEnumFromStringForDivision() {
        // given
        String division = "divide";

        // when
        BiArgumentOperation actual = from(division);

        //then
        assertThat(actual, is(DIVISION));
    }

    @Test
    public void testShouldThrowWhenUnknownOperation() {
        // given
        String unsupported = "foo";

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("Unsupported operation '%s'", unsupported));

        // when
        from(unsupported);
    }

    @Test
    public void testShouldThrowWhenNameIsNull() {
        // then
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("name of the operation is null");

        // when
        from(null);
    }
}
