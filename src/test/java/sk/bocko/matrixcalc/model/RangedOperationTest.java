package sk.bocko.matrixcalc.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static sk.bocko.matrixcalc.model.RangedOperation.AVERAGE;
import static sk.bocko.matrixcalc.model.RangedOperation.MAXIMUM;
import static sk.bocko.matrixcalc.model.RangedOperation.MINIMUM;
import static sk.bocko.matrixcalc.model.RangedOperation.PRODUCT;
import static sk.bocko.matrixcalc.model.RangedOperation.SUM;

public class RangedOperationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testShouldCreateEnumFromStringForSum() {
        // given
        String sum = "sum";

        // when
        RangedOperation actual = RangedOperation.from(sum);

        //then
        assertThat(actual, is(SUM));
    }

    @Test
    public void testShouldCreateEnumFromStringForProduct() {
        // given
        String product = "product";

        // when
        RangedOperation actual = RangedOperation.from(product);

        //then
        assertThat(actual, is(PRODUCT));
    }

    @Test
    public void testShouldCreateEnumFromStringForMin() {
        // given
        String min = "min";

        // when
        RangedOperation actual = RangedOperation.from(min);

        //then
        assertThat(actual, is(MINIMUM));
    }

    @Test
    public void testShouldCreateEnumFromStringForMax() {
        // given
        String max = "max";

        // when
        RangedOperation actual = RangedOperation.from(max);

        //then
        assertThat(actual, is(MAXIMUM));
    }

    @Test
    public void testShouldCreateEnumFromStringForAverage() {
        // given
        String average = "average";

        // when
        RangedOperation actual = RangedOperation.from(average);

        //then
        assertThat(actual, is(AVERAGE));
    }

    @Test
    public void testShouldThrowWhenUnknownRangeOperation() {
        // given
        String invalid = "invalid";

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(String.format("Unsupported operation '%s'", invalid));

        // when
        RangedOperation.from(invalid);
    }

    @Test
    public void testShouldThrowWhenCreatingRangeOperationFromNull() {
        //then
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("name of the operation is null");

        // when
        RangedOperation.from(null);
    }

}
