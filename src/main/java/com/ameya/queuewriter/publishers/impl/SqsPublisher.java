package com.ameya.queuewriter.publishers.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.ameya.queuewriter.exceptions.ClientException;
import com.ameya.queuewriter.exceptions.InternalException;
import com.ameya.queuewriter.exceptions.ServiceException;
import com.ameya.queuewriter.models.message.Message;
import com.ameya.queuewriter.publishers.MessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.MapUtils;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Publishes messages to SQS queue.
 * <p>
 * Requires:
 * sqs - An AWS SQS client with credentials that have permissions to call SendMessage API on SQS.
 * queueName - The queueName to publish to. The queue name will be looked up in the same region that the client is
 * configured to use.
 */
@RequiredArgsConstructor
@Log4j2
public class SqsPublisher implements MessagePublisher {
    private final AmazonSQS sqs;
    private final String queueName;

    @Override
    public void publish(final Message message) {
        try {
            publishUnchecked(message);
        } catch (final AmazonServiceException e) {
            if (e.getErrorCode().startsWith("4")) {
                throw new ClientException(String.format(
                        "Could not publish message [messageId=%s] to SQS queue because of exception: [%s]",
                        message.getId(), e.getMessage()), e);
            } else if (e.getErrorCode().startsWith("5")) {
                throw new ServiceException(String.format(
                        "Could not publish message [messageId=%s] to SQS queue because of exception: [%s]," +
                                " please retry in some time.", message.getId(), e.getMessage(), e));
            } else {
                throw new InternalException(
                        "Publishing message to SQS queue failed due to internal error, please investigate", e);
            }
        }
    }

    private void publishUnchecked(final Message message) {
        GetQueueUrlResult queueUrlResult = sqs.getQueueUrl(queueName);
        log.debug("Publishing message [messageId={}] to SQS endpoint {}", message.getId(), queueUrlResult.getQueueUrl());
        Map<String, MessageAttributeValue> attributes = convertAttributesToSqsAttributes(message.getAttributes());
        SendMessageRequest request = new SendMessageRequest()
                .withMessageBody(message.getBody())
                .withMessageAttributes(attributes)
                .withQueueUrl(queueUrlResult.getQueueUrl());
        sqs.sendMessage(request);
        log.debug("Successfully published message [messageId={}] to SQS", message.getId());
    }

    private Map<String, MessageAttributeValue> convertAttributesToSqsAttributes(final Map<String, String> attributes) {
        if (MapUtils.isEmpty(attributes)) {
            return Collections.emptyMap();
        }

        return attributes.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<String, MessageAttributeValue>(entry.getKey(),
                        new MessageAttributeValue().withDataType("String").withStringValue(entry.getValue())))
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }
}
