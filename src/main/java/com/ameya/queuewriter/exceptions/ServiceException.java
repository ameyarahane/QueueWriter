package com.ameya.queuewriter.exceptions;

/**
 * An exception class that indicates the cause is something that went wrong on the service side that may be rectified by
 * retrying. Corresponds to HTTP 5XX status codes.
 */
public class ServiceException extends RuntimeException {
    public ServiceException(final String message) {
        super(message);
    }

    public ServiceException(final Throwable cause) {
        super(cause);
    }

    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
