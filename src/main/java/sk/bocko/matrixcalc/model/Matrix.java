package sk.bocko.matrixcalc.model;

import com.google.common.base.MoreObjects;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Arrays;
import org.json.JSONArray;

/**
 * Created by stefan on 3/22/16.
 */
public class Matrix {
    private long[][] matrix;

    private Matrix(final long[][] matrix) {
        this.matrix = validate(matrix);
    }

    public static Matrix from(JSONArray json) {
        long[][] deserialized = new long[json.length()][];

        for (int i = 0; i < json.length(); i++) {
            JSONArray row = json.getJSONArray(i);
            deserialized[i] = new long[row.length()];
            for (int j = 0; j < row.length(); j++) {
                deserialized[i][j] = row.getLong(j);
            }
        }

        return new Matrix(deserialized);
    }

    public long[][] getMatrix() {
        return matrix;
    }

    private long[][] validate(final long[][] matrix) {
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
