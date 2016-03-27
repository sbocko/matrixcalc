package sk.bocko.matrixcalc.model;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.function.BiFunction;

/**
 * Supported binary operations.
 */
public enum BiArgumentOperation {
    ADDITION((first, second) -> first + second),
    SUBTRACTION((first, second) -> first - second),
    MULTIPLICATION((first, second) -> first * second),
    DIVISION((first, second) -> first / second);

    private final BiFunction<Double, Double, Double> operation;

    BiArgumentOperation(BiFunction<Double, Double, Double> operation) {
        this.operation = operation;
    }

    /**
     * Obtain an instance from corresponding String value.
     *
     * @param name name of the operation -> add, subtract, multiply, divide
     * @return {@link BiArgumentOperation} instance
     */
    public static BiArgumentOperation from(String name) {
        checkNotNull(name, "name of the operation is null");

        switch (name) {
            case "add":
                return ADDITION;
            case "subtract":
                return SUBTRACTION;
            case "multiply":
                return MULTIPLICATION;
            case "divide":
                return DIVISION;
            default:
                throw new IllegalArgumentException(
                    String.format("Unsupported operation '%s'", name));
        }
    }

    public BiFunction<Double, Double, Double> getOperation() {
        return operation;
    }
}
