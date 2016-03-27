package sk.bocko.matrixcalc.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PositionTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testValueOfShouldCreateCorrectInstance() {
        // given
        String expected = "12-25";

        // when
        Position actual = Position.valueOf(expected);

        // then
        assertThat(actual.getRow(), is(12));
        assertThat(actual.getColumn(), is(25));
    }

    @Test
    public void testValueOfShouldThrowWhenRowIsDecimal() {
        // given
        String invalid = "12.3-25";

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid position", invalid));

        // when
        Position.valueOf(invalid);
    }

    @Test
    public void testValueOfShouldThrowWhenColumnIsDecimal() {
        // given
        String invalid = "12-25.6";

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid position", invalid));

        // when
        Position.valueOf(invalid);
    }

    @Test
    public void testValueOfShouldThrowWhenIncorrectFormat() {
        // given
        String invalid = "foo";

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid position", invalid));

        // when
        Position.valueOf(invalid);
    }

    @Test
    public void testValueOfShouldThrowWhenRowIsMissing() {
        // given
        String withoutRow = "-2";

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid position", withoutRow));

        // when
        Position.valueOf(withoutRow);
    }

    @Test
    public void testValueOfShouldThrowWhenColumnIsMissing() {
        // given
        String withoutColumn = "1-";

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("'%s' is not a valid position", withoutColumn));

        // when
        Position.valueOf(withoutColumn);
    }

    @Test
    public void testValueOfShouldThrowWhenNullObtained() {
        //then
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("position is null");

        // when
        Position.valueOf(null);
    }
}
