package com.ameya.queuewriter.guice;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.PredefinedClientConfigurations;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Module containing all providers for AWS Services and related constructs
 */
public class AwsModule extends AbstractModule {

    @Provides
    @Singleton
    public Regions providesAwsRegion() {
        return Regions.US_WEST_2;
    }

    @Provides
    @Singleton
    public AmazonSQS providesSqsClient(final ClientConfiguration config, final AWSCredentialsProvider creds,
                                       final Regions region) {
        AmazonSQSClientBuilder builder =
                AmazonSQSClientBuilder.standard().withClientConfiguration(config).withCredentials(creds)
                        .withRegion(region);
        return builder.build();
    }

    @Provides
    @Singleton
    public AWSCredentialsProvider providesCredentials() {
        return new ProfileCredentialsProvider("default");
    }

    @Provides
    @Singleton
    public ClientConfiguration providesAwsClientConfiguration() {
        return PredefinedClientConfigurations.defaultConfig();
    }

    @Override
    protected void configure() {

    }
}
