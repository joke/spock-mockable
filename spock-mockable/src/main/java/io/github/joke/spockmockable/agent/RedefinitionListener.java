package io.github.joke.spockmockable.agent;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.builder.AgentBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Slf4j
@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
public class RedefinitionListener extends AgentBuilder.RedefinitionStrategy.Listener.Adapter {

    @Override
    public void onComplete(final int amount, final List<Class<?>> types, final Map<List<Class<?>>, Throwable> failures) {
        log.debug("Successfully transformed classes: '{}'", types);
    }

    @Override
    public Iterable<? extends List<Class<?>>> onError(final int index, final List<Class<?>> batch, final Throwable throwable, final List<Class<?>> types) {
        log.warn("Could not transform classes: '{}'", types, throwable);
        return super.onError(index, batch, throwable, types);
    }
}
