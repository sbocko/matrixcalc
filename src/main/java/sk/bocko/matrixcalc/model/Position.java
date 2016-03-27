package sk.bocko.matrixcalc.model;

import static com.google.common.base.Preconditions.checkNotNull;
import org.apache.http.annotation.ThreadSafe;

/**
 * Contains a position of an element in the matrix.
 */
@ThreadSafe
public class Position {
    public static final String DELIMITER = "-";
    private final int row, column;

    private Position(final int row, final int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Create an instance of {@link Position} class from a given string.
     * The format if '%d-%d' (e.g. 12-25).
     *
     * @param position position string to parse
     * @return {@link Position} object
     */
    public static Position valueOf(String position) {
        checkNotNull(position, "position is null");
        IllegalArgumentException invalidIndex = new IllegalArgumentException(
            String.format("'%s' is not a valid position", position));

        if (!position.contains(DELIMITER)) {
            throw invalidIndex;
        }

        String[] split = position.split(DELIMITER);
        if (split.length != 2) {
            throw invalidIndex;
        }

        try {
            int row = Integer.parseInt(split[0]);
            int column = Integer.parseInt(split[1]);
            return new Position(row, column);
        } catch (NumberFormatException ignored) {
            throw invalidIndex;
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
