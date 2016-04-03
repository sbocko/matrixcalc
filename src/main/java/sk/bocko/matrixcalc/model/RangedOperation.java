package sk.bocko.matrixcalc.model;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Supported ranged operations.
 */
public enum RangedOperation {
    SUM(values -> Arrays.stream(values).sum()),
    PRODUCT(values -> Arrays.stream(values)
        .reduce((x, y) -> x * y)
        .getAsDouble()),
    MINIMUM(values -> Arrays.stream(values).min().getAsDouble()),
    MAXIMUM(values -> Arrays.stream(values).max().getAsDouble()),
    AVERAGE(values -> Arrays.stream(values).average().getAsDouble());

    private final Function<double[], Double> operation;

    RangedOperation(Function<double[], Double> operation) {
        this.operation = operation;
    }

    /**
     * Obtain an instance from corresponding String value.
     *
     * @param name name of the operation -> sum, product, min, max, average
     * @return {@link RangedOperation} instance
     */
    public static RangedOperation from(String name) {
        checkNotNull(name, "name of the operation is null");

        switch (name) {
            case "sum":
                return SUM;
            case "product":
                return PRODUCT;
            case "min":
                return MINIMUM;
            case "max":
                return MAXIMUM;
            case "average":
                return AVERAGE;
            default:
                throw new IllegalArgumentException(
                    String.format("Unsupported operation '%s'", name));
        }
    }

    public Function<double[], Double> getOperation() {
        return operation;
    }
}
