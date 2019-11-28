package com.ameya.guice;

import com.ameya.models.message.ApiDirectory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class EnvironmentModule extends AbstractModule {

    @Provides
    @Singleton
    public ObjectMapper providesObjectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Singleton
    public ApiDirectory providesApi(final ObjectMapper objectMapper) {
        return new ApiDirectory(objectMapper);
    }

    @Override
    protected void configure() {

    }
}
