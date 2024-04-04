package bg.sofia.uni.fmi.mjt.cooking.request;

import java.net.URI;
import java.net.URISyntaxException;

public interface Request {

    /**
     * Builds and returns the URI for the request
     *
     * @throws URISyntaxException if the desired string cannot be converted to URI
     */
    URI getUri() throws URISyntaxException;
}
