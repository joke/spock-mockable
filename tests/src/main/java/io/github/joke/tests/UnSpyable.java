package io.github.joke.tests;

final public class UnSpyable {

    public final String getName() {
        return UnSpyable.class.getName();
    }

    protected final String getSimpleName() {
        return UnSpyable.class.getSimpleName();
    }

    private final String getCanonicalName() {
        return UnSpyable.class.getCanonicalName();
    }
}
