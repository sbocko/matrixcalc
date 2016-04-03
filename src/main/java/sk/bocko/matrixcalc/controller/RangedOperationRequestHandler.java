package sk.bocko.matrixcalc.controller;

import static com.google.common.base.Preconditions.checkNotNull;
import com.sun.istack.internal.Nullable;
import java.util.Optional;
import java.util.function.Function;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import sk.bocko.matrixcalc.model.Matrix;
import sk.bocko.matrixcalc.model.Range;
import sk.bocko.matrixcalc.model.RangedOperation;

/**
 * Matrix implementation of {@link UnaryOperationRequestHandler}.
 */
@Service("unary_matrix_operation")
public class RangedOperationRequestHandler implements UnaryOperationRequestHandler {
    private static final String MATRIX = "matrix";
    private static final String RESULT = "result";

    /**
     * Handles unary matrix operations.
     * @param body request body
     * @param range range of the matrix to apply operation on.
     * Whole matrix if null.
     * @param operation operation to perform on operand
     * @return
     */
    @Override
    public JSONObject handle(final String body,
        final @Nullable String range,
        final String operation) {
        checkNotNull(body, "request body is null");

        Matrix matrix = parseMatrix(body);
        Function<double[], Double> toApply = RangedOperation
            .from(operation)
            .getOperation();

        Range matrixRange = Range.valueOf(Optional.ofNullable(range));
        double result = toApply.apply(matrix.getRange(matrixRange));

        JSONObject response = new JSONObject();
        if (Double.isFinite(result)) {
            return response.put(RESULT, result);
        }

        throw new IllegalArgumentException("result is not a finite number");
    }

    private Matrix parseMatrix(final String content) {
        JSONObject json = new JSONObject(content);

        if (!json.has(MATRIX)) {
            throw new IllegalArgumentException(String.format(
                "Matrix is not present in request body [%s].", content));
        }

        return Matrix.from(json.getJSONArray(MATRIX));
    }
}
