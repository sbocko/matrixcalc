package sk.bocko.matrixcalc.model;

import java.util.Optional;
import org.apache.http.annotation.ThreadSafe;

/**
 * Represents a range in the matrix.
 */
@ThreadSafe
public final class Range {
    public static final String DELIMITER = "-";
    public static final String WILDCARD = "x";

    private final Optional<Integer> row, column;

    private Range(final Optional<Integer> row, final Optional<Integer> column) {
        this.row = row;
        this.column = column;
    }

    private Range() {
        this(Optional.empty(), Optional.empty());
    }

    /**
     * Create an instance of {@link Range} class from a given string.
     * The format if '%d-%d' (e.g. 12-25).
     * @param value range string to parse.
     * Unbounded range is returned if not present.
     * @return {@link Range} object
     */
    public static Range valueOf(Optional<String> value) {
        if (!value.isPresent()) {
            return new Range();
        }
        String range = value.get();

        IllegalArgumentException invalidIndex = new IllegalArgumentException(
             String.format("'%s' is not a valid range", range));

        if (!range.contains(DELIMITER)) {
            throw invalidIndex;
        }

        String[] split = range.split(DELIMITER);
        if (split.length != 2) {
            throw invalidIndex;
        }

        if (WILDCARD.equals(split[1])) {
            try {
                int row = Integer.parseInt(split[0]);
                return new Range(Optional.of(row), Optional.empty());
            } catch (NumberFormatException ignored) {
                throw invalidIndex;
            }
        } else if (WILDCARD.equals(split[0])) {
            try {
                int column = Integer.parseInt(split[1]);
                return new Range(Optional.empty(), Optional.of(column));
            } catch (NumberFormatException ignored) {
                throw invalidIndex;
            }
        }
        throw invalidIndex;
    }

    boolean isWholeMatrix() {
        return !isRow() && !isColumn();
    }

    boolean isRow() {
        return row.isPresent();
    }

    boolean isColumn() {
        return column.isPresent();
    }

    int getValue() {
        if (isRow()) {
            return row.get();
        } else if (isColumn()) {
            return column.get();
        }
        throw new IllegalStateException("range has no value");
    }

    @Override
    public String toString() {
        if (isRow()) {
            return getValue() + "-x";
        } else if (isColumn()) {
            return "x-" + getValue();
        }
        return "unbounded range";
    }
}
