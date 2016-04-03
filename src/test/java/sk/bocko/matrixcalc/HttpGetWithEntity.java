package sk.bocko.matrixcalc;

import java.net.URI;
import org.apache.http.client.methods.HttpPost;

/**
 * Helper class for testing Http.GET with request body.
 */
public class HttpGetWithEntity extends HttpPost {
    private static final String METHOD_NAME = "GET";

    public HttpGetWithEntity(URI url) {
        super(url);
    }

    public HttpGetWithEntity(String url) {
        super(url);
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }
}
