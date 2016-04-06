package sk.bocko.matrixcalc.log;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestLogger {

    private final Logger log;

    public RequestLogger(final Class<?> clazz) {
        checkNotNull(clazz, "class is null");
        this.log = LoggerFactory.getLogger(clazz);
    }

    public void logSuccessfulResponse(HttpServletRequest request, String body, String response) {
        checkNotNull(request, "request is null");

        log.info(String.format("Returned response: %s for %s request from %s for path %s content type %s body %s.",
            response,
            request.getMethod(),
            request.getRemoteHost(),
            request.getServletPath(),
            request.getHeader("content-type"),
            body));
    }

    public void logError(HttpServletRequest request, Exception e) {
        checkNotNull(request, "request is null");
        checkNotNull(e, "exception is null");

        log.error(String.format("Exception with message: %s thrown while "
                + "processing %s request from %s for path %s content type %s.",
            e.getMessage(),
            request.getMethod(),
            request.getRemoteHost(),
            request.getServletPath(),
            request.getHeader("content-type")));
    }
}
