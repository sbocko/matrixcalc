package sk.bocko.matrixcalc.controller;

import java.util.function.BiFunction;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sk.bocko.matrixcalc.model.BiArgumentOperation;
import sk.bocko.matrixcalc.model.Matrix;
import sk.bocko.matrixcalc.model.Position;

/**
 * Matrix implementation of {@link BinaryOperationRequestHandler}.
 */
@Service("binary_matrix_operation")
public class BinaryOperationRequestHandlerImpl
    implements BinaryOperationRequestHandler{

    private static final Logger LOG =
        LoggerFactory.getLogger(BinaryOperationRequestHandlerImpl.class);
    private static final String MATRIX = "matrix";
    private static final String RESULT = "result";

    /**
     * Parse request arguments, calculate result and return a json response.
     *
     * @param body request body with matrix to perform operations on
     * @param firstOperandIndex position of first operand in a matrix
     * @param secondOperandIndex position of second operand in a matrix
     * @param operation operation to perform on operands
     * @return json with result
     */
    @Override
    public JSONObject handle(
        String body,
        String firstOperandIndex,
        String secondOperandIndex,
        String operation) {

        Matrix matrix = parseMatrix(body);
        double first = getElementForIndex(firstOperandIndex, matrix);
        double second = getElementForIndex(secondOperandIndex, matrix);
        BiFunction<Double, Double, Double> toApply =
            BiArgumentOperation.from(operation).getOperation();

        double result = toApply.apply(first, second);
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

    private double getElementForIndex(String index, Matrix matrix) {
        Position position = Position.valueOf(index);
        return matrix.valueAtPosition(position);
    }
}
