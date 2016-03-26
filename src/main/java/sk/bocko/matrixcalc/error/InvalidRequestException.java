package sk.bocko.matrixcalc.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by stefan on 3/25/16.
 */
@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class InvalidRequestException extends  Exception {

    public InvalidRequestException(final String message) {
        super(message);
    }
}
