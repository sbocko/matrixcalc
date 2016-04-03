package sk.bocko.matrixcalc.controller;

import java.util.Optional;
import java.util.function.Function;
import org.json.JSONObject;
import sk.bocko.matrixcalc.model.Matrix;
import sk.bocko.matrixcalc.model.Range;
import sk.bocko.matrixcalc.model.RangedOperation;

public class RangedOperationRequestHandler {
    private static final String MATRIX = "matrix";
    private static final String RESULT = "result";

    JSONObject handle(final String body,
        final String range,
        final String operation) {

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
