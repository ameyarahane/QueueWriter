package com.ameya.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Arrays;

public class Injectors {

    public static Injector createDefaultInjector() {
        return Guice.createInjector(Arrays.asList(new EnvironmentModule(), new AwsModule(), new MessagePublishersModule()));
    }
}
