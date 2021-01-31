package io.github.joke.spockmockable.internal;

import org.junit.platform.engine.EngineDiscoveryListener;

public class EngineDiscovery implements EngineDiscoveryListener {
    private static final MockableExtension mockableExtension = new MockableExtension();
}
