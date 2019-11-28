package com.ameya.queuewriter.activities;

import com.ameya.queuewriter.exceptions.ClientException;
import com.ameya.queuewriter.exceptions.InternalException;
import com.ameya.queuewriter.exceptions.ServiceException;
import com.ameya.queuewriter.models.message.Message;
import com.ameya.queuewriter.models.requests.AbstractRequest;
import com.ameya.queuewriter.models.requests.SendMessageRequest;
import com.ameya.queuewriter.publishers.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;


@RequiredArgsConstructor
@Log4j2
public class SendMessageActivity extends AbstractActivity<Void> {

    private static final String CLIENT_ERROR = "4XX";
    private static final String SERVER_ERROR = "5XX";
    private static final String INTERNAL_ERROR = "500";
    private final MessagePublisher publisher;

    @Override
    protected Void processRequest(final AbstractRequest abstractRequest) {
        SendMessageRequest request = (SendMessageRequest) abstractRequest;
        handleRequest(request);
        return null;
    }

    /**
     * Publishes the message passed in to the publisher associated with the service.
     * <p>
     * Returns successfully if the operation was successful else throws the appropriate exception back to the client.
     */
    private void handleRequest(final SendMessageRequest request) {
        String requestId = request.getRequestId();
        Message message = request.getMessage();
        validate(message, requestId);

        log.info("[requestId:{} START: Processing message with id={}", requestId, message.getId());
        boolean isSuccessful = false;
        try {
            publisher.publish(message);
            isSuccessful = true;
        } catch (ClientException e) {
            String errorMessage = getErrorMessage(message, requestId, CLIENT_ERROR, e);
            log.error(errorMessage, e);
            throw new ClientException(e.getMessage());
        } catch (ServiceException e) {
            String errorMessage = getErrorMessage(message, requestId, SERVER_ERROR, e);
            log.error(errorMessage, e);
            throw new ServiceException(e.getMessage());
        } catch (RuntimeException e) {
            String errorMessage = getErrorMessage(message, requestId, INTERNAL_ERROR, e);
            log.error(errorMessage, e);
            throw new InternalException(e.getMessage());
        } finally {
            log.info("[requestId:{}] END: Message processed {} for id={}", requestId,
                    isSuccessful ? "successfully" : "unsuccessfully", message.getId());
        }
    }

    private String getErrorMessage(final Message message, final String requestId, final String errorCode,
                                   final RuntimeException e) {
        return String.format(
                "Encountered %s [statusCode=%s] when processing request [requestId=%s] with message [messageId=%s]: %s",
                e.getClass().getSimpleName(), errorCode, requestId, message.getId(), e.getMessage());
    }

    private void validate(final Message message, final String requestId) {
        Objects.requireNonNull(message, "Message must be non-null");

        String exceptionMessage = null;
        if (StringUtils.isEmpty(message.getId())) {
            exceptionMessage = "Message ID field must be non-null and non-empty";
        } else if (StringUtils.isEmpty(message.getBody())) {
            exceptionMessage = "Message body field must be non-null and non-empty";
        }

        if (exceptionMessage != null) {
            throw new IllegalArgumentException(String.format("[requestId:%s] %s", requestId, exceptionMessage));
        }
    }
}
