package com.ameya.exceptions;

/**
 * A non-retryable exception that indicates the cause is invalid conditions on the client side that need to be
 * fixed before retrying. Corresponds to HTTP 4XX status codes.
 */
public class ClientException extends RuntimeException {
    public ClientException(final String message) {
        super(message);
    }

    public ClientException(final Throwable cause) {
        super(cause);
    }

    public ClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
