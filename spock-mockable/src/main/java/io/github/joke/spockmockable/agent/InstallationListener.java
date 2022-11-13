package io.github.joke.spockmockable.agent;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import org.spockframework.runtime.extension.ExtensionException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.Set;

@Slf4j
@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
class InstallationListener extends AgentBuilder.InstallationListener.Adapter {

    @Override
    public Throwable onError(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer, final Throwable throwable) {
        throw new ExtensionException("Unable install spock-mockable transformation", throwable);
    }

    @Override
    public void onBeforeInstall(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer) {
        log.trace("onBeforeInstall");
    }

    @Override
    public void onInstall(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer) {
        log.trace("onInstall");
    }

    @Override
    public void onReset(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer) {
        log.trace("onReset");
    }

    @Override
    public void onBeforeWarmUp(final Set<Class<?>> types, final ResettableClassFileTransformer classFileTransformer) {
        log.trace("onBeforeWarmUp");
        super.onBeforeWarmUp(types, classFileTransformer);
    }

    @Override
    public void onWarmUpError(final Class<?> type, final ResettableClassFileTransformer classFileTransformer, final Throwable throwable) {
        log.trace("onWarmUpError");
        super.onWarmUpError(type, classFileTransformer, throwable);
    }

    @Override
    public void onAfterWarmUp(final Map<Class<?>, byte[]> types, final ResettableClassFileTransformer classFileTransformer, final boolean transformed) {
        log.trace("onBeforeInstall");
        super.onAfterWarmUp(types, classFileTransformer, transformed);
    }
}
