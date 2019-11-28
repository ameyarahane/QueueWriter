package com.ameya.guice;

import com.amazonaws.services.sqs.AmazonSQS;
import com.ameya.messages.publisher.LogMessagePublisher;
import com.ameya.messages.publisher.MessagePublisher;
import com.ameya.messages.publisher.SqsPublisher;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class MessagePublishersModule extends AbstractModule {

    @Provides
    @Singleton
    @Named("LogMessagePublisher")
    public MessagePublisher providesLogMessagePublisher() {
        return new LogMessagePublisher();
    }

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
