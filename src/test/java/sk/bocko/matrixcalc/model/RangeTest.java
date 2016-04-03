package sk.bocko.matrixcalc.model;

import java.util.Optional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RangeTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testShouldCreateRangeInstanceForRow() throws Exception {
        // given
        Optional<String> range = Optional.of("12-x");

        // when
        Range actual = Range.valueOf(range);

        //then
        assertThat(actual.isRow(), is(true));
        assertThat(actual.isColumn(), is(false));
        assertThat(actual.isWholeMatrix(), is(false));
        assertThat(actual.getValue(), is(12));
    }

    @Test
    public void testShouldCreateRangeInstanceForColumn() throws Exception {
        // given
        Optional<String> range = Optional.of("x-25");

        // when
        Range actual = Range.valueOf(range);

        //then
        assertThat(actual.isColumn(), is(true));
        assertThat(actual.isRow(), is(false));
        assertThat(actual.isWholeMatrix(), is(false));
        assertThat(actual.getValue(), is(25));
    }

    @Test
    public void testShouldCreateRangeInstanceWhenWholeMatrix() throws Exception {
        // given
        Optional<String> empty = Optional.empty();

        // when
        Range actual = Range.valueOf(empty);

        //then
        assertThat(actual.isWholeMatrix(), is(true));
        assertThat(actual.isColumn(), is(false));
        assertThat(actual.isRow(), is(false));
    }

    @Test
    public void testCreateInstanceShouldThrowWhenIndexCalledOnWholeMatrixRange() throws Exception {
        // given
        Optional<String> empty = Optional.empty();

        //then
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("range has no value");

        // when
        Range actual = Range.valueOf(empty);
        actual.getValue();
    }

    @Test
    public void testCreateInstanceShouldThrowWhenInvalidColumn() throws Exception {
        // given
        Optional<String> invalid = Optional.of("x-invalid");

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid range", invalid.get()));

        // when
        Range actual = Range.valueOf(invalid);
        actual.getValue();
    }

    @Test
    public void testCreateInstanceShouldThrowWhenInvalidRow() throws Exception {
        // given
        Optional<String> invalid = Optional.of("-x");
        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid range", invalid.get()));

        // when
        Range actual = Range.valueOf(invalid);
        actual.getValue();
    }

    @Test
    public void testShouldCreateInstanceThrowWhenNoWildcardPresent() throws Exception {
        // given
        Optional<String> noWildcard = Optional.of("12-25");

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid range", noWildcard.get()));

        // when
        Range.valueOf(noWildcard);
    }

    @Test
    public void testShouldCreateInstanceThrowWhenBothWildcards() throws Exception {
        // given
        Optional<String> noWildcard = Optional.of("x-x");

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid range", noWildcard.get()));

        // when
        Range.valueOf(noWildcard);
    }

}
