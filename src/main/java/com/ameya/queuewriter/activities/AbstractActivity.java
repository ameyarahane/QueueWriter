package com.ameya.queuewriter.activities;

import com.ameya.queuewriter.models.requests.AbstractRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class AbstractActivity<T> {

    /**
     * Handler for handling all API requests. Classes for specific APIs must implement the processRequest method.
     */
    public T handleRequest(final AbstractRequest request) {
        String requestId = request.getRequestId();
        log.info("START: Processing request={}", requestId);
        T result = null;
        try {
            result = processRequest(request);
        } finally {
            log.info("END: Processing request={}", requestId);
        }

        return result;
    }

    abstract T processRequest(AbstractRequest request);

}
