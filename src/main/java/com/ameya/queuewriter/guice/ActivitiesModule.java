package com.ameya.queuewriter.guice;

import com.ameya.queuewriter.activities.DescribeApiActivity;
import com.ameya.queuewriter.activities.ListApisActivity;
import com.ameya.queuewriter.activities.SendMessageActivity;
import com.ameya.queuewriter.metadata.ApiMetadataProvider;
import com.ameya.queuewriter.metadata.impl.SimpleApiMetadataProvider;
import com.ameya.queuewriter.publishers.MessagePublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class ActivitiesModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    public SendMessageActivity providesSendMessageActivity(
            @Named("SqsMessagePublisher") final MessagePublisher messagePublisher) {
        return new SendMessageActivity(messagePublisher);
    }

    @Provides
    @Singleton
    public ListApisActivity providesListApisActivity(final ObjectMapper objectMapper,
                                                     final ApiMetadataProvider metadataProvider) {
        return new ListApisActivity(objectMapper, metadataProvider);
    }

    @Provides
    @Singleton
    public DescribeApiActivity providesDescribeApiActivity(final ObjectMapper objectMapper,
                                                           final ApiMetadataProvider metadataProvider) {
        return new DescribeApiActivity(objectMapper, metadataProvider);
    }

    @Provides
    @Singleton
    public ObjectMapper providesObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public ApiMetadataProvider providesApi(final ObjectMapper objectMapper) {
        return new SimpleApiMetadataProvider(objectMapper);
    }
}
