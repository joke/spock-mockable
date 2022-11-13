package io.github.joke.spockmockable.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class TransformationStarter {

    final ReferenceLoader referenceLoader;

    final ClassTransformer classTransformer;

    AtomicBoolean started = new AtomicBoolean(false);

    public void start() {
        if (started.getAndSet(true)) {
            return;
        }
        if (parseBoolean(getProperty("spock-mockable.disabled", "false"))) {
            log.info("@Mockable transformation is disabled by system property 'spock-mockable.disabled=true'");
            return;
        }

        if (referenceLoader.hasClasses()) {
            classTransformer.applyTransformation();
        }
    }
}
