package sk.bocko.matrixcalc.model;

/**
 * Contains error message returned by the Rest service when an error occures.
 */
public class ErrorResponse {

    private final String error;

    public ErrorResponse(final String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
