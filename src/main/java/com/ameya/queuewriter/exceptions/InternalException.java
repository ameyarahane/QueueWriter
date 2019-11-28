package com.ameya.queuewriter.exceptions;

/**
 * An exception class that covers all exception types not covered by the other exception classes. This is indicative of
 * an underlying issue with the service that needs to be fixed to resume normal operation.
 */
public class InternalException extends RuntimeException {
    public InternalException(final String message) {
        super(message);
    }

    public InternalException(final Throwable cause) {
        super(cause);
    }

    public InternalException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
