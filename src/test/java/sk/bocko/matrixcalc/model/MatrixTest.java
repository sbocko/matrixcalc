package sk.bocko.matrixcalc.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class MatrixTest {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Test
    public void testMatrixFromJsonShouldSucceedWhenStandardMatrix() {
        // given
        JSONArray matrix = new JSONArray("[[1,2.3],[4.5,6]]");

        // when
        Matrix actual = Matrix.from(matrix);

        //then
        final double[][] expected = new double[][]{{1, 2.3}, {4.5, 6}};
        assertThat(actual.getMatrix(), is(expected));
    }

    @Test
    public void testMatrixFromJsonShouldSucceedWhenOneRowMatrix() {
        // given
        JSONArray matrix = new JSONArray("[[1,2.3]]");

        // when
        Matrix actual = Matrix.from(matrix);

        //then
        final double[][] expected = new double[][]{{1, 2.3}};
        assertThat(actual.getMatrix(), is(expected));
    }

    @Test
    public void testMatrixFromJsonShouldSucceedWhenOneColumnMatrix() {
        // given
        JSONArray matrix = new JSONArray("[[1],[2.3]]");

        // when
        Matrix actual = Matrix.from(matrix);

        //then
        final double[][] expected = new double[][]{{1},{2.3}};
        assertThat(actual.getMatrix(), is(expected));
    }

    @Test
    public void testMatrixFromJsonShouldThrowWhenEmptyArray() {
        // given
        JSONArray matrix = new JSONArray("[]");

        //then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("matrix is empty");

        // when
        Matrix.from(matrix);
    }

    @Test
    public void testMatrixFromJsonShouldThrowWhenArrayIsNotNumeric() {
        // given
        JSONArray matrix = new JSONArray("[[1,2],[invalid, 4]]");

        //then
        thrown.expect(JSONException.class);
        thrown.expectMessage("not a number");

        // when
        Matrix.from(matrix);
    }

    @Test
    public void testValueAtPositionShouldReturnCorrectElement() {
        // given
        Matrix matrix = Matrix.from(new JSONArray("[[1, 2],[3, 4]]"));
        Position position = Position.valueOf("2-1");

        // when
        double actual = matrix.valueAtPosition(position);

        // then
        assertThat(actual, is(3d));
    }

    @Test
    public void testValueAtPositionShouldThrowWhenRowIsSmallerThanOne() {
        // given
        Matrix matrix = Matrix.from(new JSONArray("[[1, 2],[3, 4]]"));
        Position position = Position.valueOf("0-1");

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The '0-1' position is out-of-range for the matrix.");

        // when
        matrix.valueAtPosition(position);
    }

    @Test
    public void testValueAtPositionShouldThrowWhenColumnIsSmallerThanOne() {
        // given
        Matrix matrix = Matrix.from(new JSONArray("[[1, 2],[3, 4]]"));
        Position position = Position.valueOf("2-0");

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The '2-0' position is out-of-range for the matrix.");

        // when
        matrix.valueAtPosition(position);
    }

    @Test
    public void testValueAtPositionShouldThrowWhenRowIsTooLarge() {
        // given
        Matrix matrix = Matrix.from(new JSONArray("[[1, 2],[3, 4]]"));
        Position position = Position.valueOf("3-1"); // third row in 2x2 matrix

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The '3-1' position is out-of-range for the matrix.");

        // when
        matrix.valueAtPosition(position);
    }

    @Test
    public void testValueAtPositionShouldThrowWhenColumnIsTooLarge() {
        // given
        Matrix matrix = Matrix.from(new JSONArray("[[1, 2],[3, 4]]"));
        Position position = Position.valueOf("2-3"); // third column in 2x2 matrix

        // then
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("The '2-3' position is out-of-range for the matrix.");

        // when
        matrix.valueAtPosition(position);
    }
}