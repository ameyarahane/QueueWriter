package com.ameya.queuewriter.guice;

import com.amazonaws.services.sqs.AmazonSQS;
import com.ameya.queuewriter.publishers.MessagePublisher;
import com.ameya.queuewriter.publishers.impl.LogMessagePublisher;
import com.ameya.queuewriter.publishers.impl.SqsPublisher;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Guice module for providing all dependencies related to MessagePublishers.
 */
public class MessagePublishersModule extends AbstractModule {

    /**
     * Does nothing except writing the message to a logger.
     */
    @Provides
    @Singleton
    @Named("LogMessagePublisher")
    public MessagePublisher providesLogMessagePublisher() {
        return new LogMessagePublisher();
    }

    /**
     * Publishes messages to an SQS queue matching the queueName parameter using the provided AmazonSQS client.
     */
    @Provides
    @Singleton
    @Named("SqsMessagePublisher")
    public MessagePublisher providesSqsMessagePublisher(final AmazonSQS sqsClient,
                                                        @Named("SqsQueueName") final String queueName) {
        return new SqsPublisher(sqsClient, queueName);
    }

    @Provides
    @Singleton
    @Named("SqsQueueName")
    public String providesSqsQueueName() {
        return "service-queue";
    }

    @Override
    protected void configure() {

    }
}
