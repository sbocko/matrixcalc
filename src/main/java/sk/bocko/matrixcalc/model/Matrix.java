package sk.bocko.matrixcalc.model;

import com.google.common.base.MoreObjects;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.deepToString;
import static java.util.Arrays.stream;
import org.apache.http.annotation.ThreadSafe;
import org.json.JSONArray;

/**
 * Domain model that contains the matrix to perform calculations on.
 */
@ThreadSafe
public final class Matrix {
    private double[][] matrix;

    private Matrix(final double[][] matrix) {
        this.matrix = validate(matrix);
    }

    /**
     * Create an instance of {@link Matrix} from a json array.
     * @param json array with matrix (e.g. [[1,2.3],[4,5]])
     * @return a new {@link Matrix} instance
     */
    public static Matrix from(JSONArray json) {
        double[][] deserialized = new double[json.length()][];

        for (int i = 0; i < json.length(); i++) {
            JSONArray row = json.getJSONArray(i);
            deserialized[i] = new double[row.length()];
            for (int j = 0; j < row.length(); j++) {
                deserialized[i][j] = row.getDouble(j);
            }
        }

        return new Matrix(deserialized);
    }

    /**
     * Returns an element at given position. Matrix is 1-indexed.
     * @return matrix element at index or
     * {@link IllegalArgumentException} if index is out of range
     */
    public double valueAtPosition(Position index) {
        int row = index.getRow();
        int column = index.getColumn();

        if (row < 1 || matrix.length < row ||
            column < 1 || matrix[0].length < column) {
            String message = String.format("The '%d-%d' position is " +
                "out-of-range for the matrix.", row, column);
            throw new IllegalArgumentException(message);
        }

        return matrix[row - 1][column - 1];
    }

    /**
     * Get all the values for specified range. Throws IllegalArgumentException
     * if range is not valid for a matrix.
     * @param range to get values for
     * @return an array with all the values for specified range
     */
    public double[] getRange(Range range) {
        if (range.isWholeMatrix()) {
            return matrixToArray(matrix);
        }

        int index = range.getValue();
        if (range.isRow()) {
            if (isValidRange(matrix.length, index)) {
                return copyOf(matrix[index - 1], matrix[0].length);
            }
        }

        if (range.isColumn()) {
            if (isValidRange(matrix[0].length, index)) {
                return getColumn(index);
            }
        }

        String message = String.format(
            "The '%s' range is not valid for the matrix.", range);
        throw new IllegalArgumentException(message);
    }

    public double[][] getMatrix() {
        return matrix.clone();
    }

    private double[] matrixToArray(final double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[] elements = new double[rows * columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                elements[i * rows + j] = matrix[i][j];
            }
        }
        return elements;
    }

    private boolean isValidRange(final int upperBound, final int index) {
        return !(index < 1 || upperBound < index);
    }

    private double[] getColumn(final int index) {
        double[] column = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            double[] row = matrix[i];
            column[i] = row[index - 1];
        }
        return column;
    }

    private double[][] validate(final double[][] matrix) {
        checkNotNull(matrix, "matrix is null");
        checkArgument(matrix.length > 0, "matrix is empty");
        checkRectangularity(matrix);

        return matrix;
    }

    private void checkRectangularity(final double[][] matrix) {
        int columns = matrix[0].length;

        stream(matrix).forEach(row -> {
            if (row.length != columns) {
                throw new IllegalArgumentException(
                    String.format("Matrix %s is not rectangular.",
                    deepToString(matrix)));
            }
        });
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass().getSimpleName())
            .add("matrix", matrix)
            .toString();
    }
}
