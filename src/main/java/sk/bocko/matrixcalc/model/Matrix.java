package sk.bocko.matrixcalc.model;

import com.google.common.base.MoreObjects;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Arrays;
import org.apache.http.annotation.ThreadSafe;
import org.json.JSONArray;

/**
 * Domain model that contains the matrix to perform calculations on.
 */
@ThreadSafe
public class Matrix {
    private double[][] matrix;

    private Matrix(final double[][] matrix) {
        this.matrix = validate(matrix);
    }

    /**
     * Create an instance of {@link Matrix} from a json array.
     *
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

    public double[][] getMatrix() {
        return matrix.clone();
    }

    private double[][] validate(final double[][] matrix) {
        checkNotNull(matrix, "matrix is null");
        checkArgument(matrix.length > 0, "matrix is empty");

        int columns = matrix[0].length;
        Arrays.stream(matrix).forEach(row -> {
            if (row.length != columns) {
                String message = String.format("Matrix %s is not rectangular.",
                    Arrays.deepToString(matrix));
                throw new IllegalArgumentException(message);
            }
        });

        return matrix;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass().getSimpleName())
            .add("matrix", matrix)
            .toString();
    }
}
