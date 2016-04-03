package sk.bocko.matrixcalc.controller;

import org.json.JSONObject;

/**
 * Handles two argument operation requests.
 */
public interface BinaryOperationRequestHandler {

    /**
     * Handle request.
     *
     * @param body request body
     * @param firstArgument first argument of the request
     * @param secondArgument second argument of the request
     * @param operation operation to perform on operands
     * @return json with result
     */
    JSONObject handle(
        String body,
        String firstArgument,
        String secondArgument,
        String operation);
}
