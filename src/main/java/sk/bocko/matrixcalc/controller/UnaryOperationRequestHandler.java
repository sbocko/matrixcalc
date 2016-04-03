package sk.bocko.matrixcalc.controller;

import org.json.JSONObject;

/**
 * Handles single argument operation requests.
 */
public interface UnaryOperationRequestHandler {

    /**
     * Handle request for unary operation.
     *
     * @param body request body
     * @param argument operation argument
     * @param operation operation to perform on operand
     * @return json with result
     */
    JSONObject handle(String body, String argument, String operation);
}
