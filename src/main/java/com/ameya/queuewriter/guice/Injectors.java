package com.ameya.queuewriter.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Arrays;

public class Injectors {

    /**
     * Provides a Guice injector with all modules used to fulfill dependencies.
     */
    public static Injector createDefaultInjector() {
        return Guice.createInjector(Arrays.asList(new AwsModule(), new MessagePublishersModule(),
                new ActivitiesModule()));
    }
}
