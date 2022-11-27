package io.github.joke.spockmockable.agent;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.pool.TypePool.Resolution.NoSuchTypeException;
import net.bytebuddy.utility.JavaModule;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
class DiscoveryListener extends AgentBuilder.Listener.Adapter {

    @Override
    public void onError(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded, final Throwable throwable) {
        if (throwable instanceof NoSuchTypeException) {
            log.trace("Could not resolved type description: {}", typeName, throwable);
        } else {
            log.warn("Could not transform class '{}', loaded: {}", typeName, loaded);
        }
    }

    @Override
    public void onComplete(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {
        log.trace("Processed class '{}', loaded: {}", typeName, loaded);
    }

    @Override
    public void onDiscovery(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {
        log.trace("Processing class '{}', loaded: {}", typeName, loaded);
    }

    @Override
    public void onTransformation(final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module, final boolean loaded, final DynamicType dynamicType) {
        log.trace("Transforming class '{}', loaded: {}", typeDescription, loaded);
    }

    @Override
    public void onIgnored(final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {
        log.trace("Ignoring class '{}', loaded: {}", typeDescription, loaded);
    }
}
