package io.github.joke.spockmockable.agent;

import dagger.Component;
import dagger.Provides;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.lang.instrument.Instrumentation;

@Slf4j
@Singleton
@Component(modules = MockableController.Module.class)
public abstract class MockableController {

    @Getter(lazy = true, onMethod_ = {@SuppressWarnings("NullAway")})
    private static final MockableController instance = DaggerMockableController.create();

    protected abstract TransformationStarter transformationStarter();

    public static void init() {
        getInstance()
                .transformationStarter()
                .start();
    }

    @Component.Factory
    interface Factory {

        MockableController create();
    }

    @dagger.Module
    interface Module {

        @Provides
        @Singleton
        static Instrumentation getInstrumentation() {
            return InstrumentationInstaller.getInstrumentation();
        }

    }
}
