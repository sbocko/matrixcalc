package sk.bocko.matrixcalc;

import java.net.URI;
import org.apache.http.client.methods.HttpPost;

/**
 * Created by stefan on 3/24/16.
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
