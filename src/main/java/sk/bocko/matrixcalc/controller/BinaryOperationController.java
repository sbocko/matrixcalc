package sk.bocko.matrixcalc.controller;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sk.bocko.matrixcalc.log.RequestLogger;
import sk.bocko.matrixcalc.model.ErrorResponse;

@RestController
public class BinaryOperationController {
    private static final RequestLogger LOG = new RequestLogger(BinaryOperationController.class);

    private BinaryOperationRequestHandler handler;

    @Autowired
    public BinaryOperationController(
        @Qualifier(value = "binary_matrix_operation")
            BinaryOperationRequestHandler handler) {
        this.handler = checkNotNull(handler, "handler is null");
    }

    /**
     * Process requests for binary operations as specified here:
     * http://docs.matrixcalc.apiary.io/#reference/simple-operations-with-two-operands/
     */
    @RequestMapping(
        value = "/rest/{operation:add|subtract|multiply|divide}/{firstAddentIndex}/{secondAddentIndex}",
        produces = "application/json",
        method = RequestMethod.GET)
    public String process(
        HttpServletRequest request,
        @RequestBody String content,
        @PathVariable("operation") String operation,
        @PathVariable("firstAddentIndex") String firstAddentIndex,
        @PathVariable("secondAddentIndex") String secondAddentIndex) {

        JSONObject result = handler
            .handle(content, firstAddentIndex, secondAddentIndex, operation);

        LOG.logSuccessfulResponse(request, content, result.toString());
        return result.toString();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponse handleMissingContent(HttpServletRequest request, HttpMessageNotReadableException e) {
        LOG.logError(request, e);
        return new ErrorResponse("Matrix is not present in request body.");
    }

    @ExceptionHandler(JSONException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponse handleInvalidContent(HttpServletRequest request, JSONException e) {
        LOG.logError(request, e);
        return new ErrorResponse("Request body is not a valid json: "
            + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponse handleInvalidMatrix(HttpServletRequest request, Exception e) {
        LOG.logError(request, e);
        return new ErrorResponse(e.getMessage());
    }
}
