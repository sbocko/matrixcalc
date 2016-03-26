package sk.bocko.matrixcalc.controller;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sk.bocko.matrixcalc.error.ErrorResponse;
import sk.bocko.matrixcalc.model.Matrix;

@RestController
public class AdditionController {
    public static final String MATRIX = "matrix";
    Logger LOG = LoggerFactory.getLogger(AdditionController.class);

    private static final String template = "Hello, %s, %s, %s!";

    @RequestMapping(
        value = "/rest/add/{row}/{column}",
        method = RequestMethod.GET,
        produces = "application/json")
    public String addition(@RequestBody String content,
        @PathVariable("row") String row,
        @PathVariable("column") String column) {
        JSONObject json = new JSONObject(content);
        if (!json.has(MATRIX)) {
            throw new IllegalArgumentException(String.format(
                "Matrix is not present in request body [%s].", content));
        }
        Matrix matrix = Matrix.from(json.getJSONArray(MATRIX));
        String response = String.format(template, matrix, row, column);
        LOG.info(response);
        return response;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponse handleMissingContent() {
        return new ErrorResponse("Matrix is not present in request body.");
    }

    @ExceptionHandler(JSONException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponse handleInvalidContent(JSONException e) {
        return new ErrorResponse("Request body is not a valid json: "
            + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ErrorResponse handleInvalidMatrix(HttpServletRequest req, Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
